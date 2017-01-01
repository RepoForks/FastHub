package com.fastaccess.ui.modules.contentviewer.gists.comments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.adapter.CommentsAdapter;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

public class GistCommentsPresenter extends BasePresenter<GistCommentsMvp.View> implements GistCommentsMvp.Presenter {
    private ArrayList<CommentsModel> comments = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;

    @Override public int getCurrentPage() {
        return page;
    }

    @Override public int getPreviousTotal() {
        return previousTotal;
    }

    @Override public void setCurrentPage(int page) {
        this.page = page;
    }

    @Override public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    @Override public void onCallApi(int page, @Nullable String parameter) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        if (page > lastPage || parameter == null || lastPage == 0) {
            sendToView(GistCommentsMvp.View::onHideProgress);
            return;
        }
        setCurrentPage(page);
        manageSubscription(
                RxHelper.getObserver(RestClient.getGistComments(parameter, page).asObservable())
                        .doOnSubscribe(() -> sendToView(GistCommentsMvp.View::onShowProgress))
                        .doOnNext(listResponse -> {
                            lastPage = listResponse.getLast();
                            setApiCalled();
                            if (getCurrentPage() == 1) {
                                getComments().clear();
                                manageSubscription(CommentsModel.save(parameter, listResponse.getItems()).subscribe());
                            }
                            getComments().addAll(listResponse.getItems());
                            sendToView(GistCommentsMvp.View::onNotifyAdapter);
                        })
                        .onErrorReturn(throwable -> {
                            throwable.printStackTrace();
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline(parameter);
                            return null;
                        })
                        .subscribe());
    }

    @NonNull @Override public ArrayList<CommentsModel> getComments() {
        return comments;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data,
                                           @NonNull DynamicRecyclerView recycler, @NonNull CommentsAdapter adapter) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == BundleConstant.REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    boolean isNew = bundle.getBoolean(BundleConstant.EXTRA);
                    CommentsModel commentsModel = bundle.getParcelable(BundleConstant.ITEM);
                    if (isNew) {
                        adapter.addItem(commentsModel);
                        recycler.smoothScrollToPosition(adapter.getItemCount());
                    } else {
                        int position = adapter.getItem(commentsModel);
                        if (position != -1) {
                            adapter.swapItem(commentsModel, position);
                            recycler.smoothScrollToPosition(position);
                        } else {
                            adapter.addItem(commentsModel);
                            recycler.smoothScrollToPosition(adapter.getItemCount());
                        }
                    }
                }
            }
        }
    }

    @Override public void onHandleDeletion(@Nullable Bundle bundle) {
        if (bundle != null) {
            long commId = bundle.getLong(BundleConstant.EXTRA, 0);
            String gistId = bundle.getString(BundleConstant.ID);
            if (commId != 0 && gistId != null) {
                manageSubscription(RxHelper.getObserver(RestClient.deleteGistComment(gistId, commId))
                        .doOnSubscribe(() -> sendToView(GistCommentsMvp.View::onShowProgressDialog))
                        .doOnNext(booleanResponse -> sendToView(view -> view.onHandleCommentDelete(booleanResponse, commId)))
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            return null;
                        })
                        .subscribe());
            }
        }
    }

    @Override public void onWorkOffline(@NonNull String gistId) {
        if (comments.isEmpty()) {
            manageSubscription(
                    CommentsModel.getComments(gistId)
                            .subscribe(localComments -> {
                                if (localComments != null && !localComments.isEmpty()) {
                                    Logger.e(localComments.size());
                                    comments.addAll(localComments);
                                    sendToView(GistCommentsMvp.View::onNotifyAdapter);
                                }
                            })
            );
        }
    }

    @Override public void onItemClick(int position, View v, CommentsModel item) {
        if (item.getUser() != null) {
            UserModel userModel = UserModel.getUser();
            if (userModel != null && item.getUser().getLogin().equals(userModel.getLogin())) {
                if (getView() != null) getView().onEditComment(item);
            } else {
                if (getView() != null) getView().onTagUser(item.getUser());
            }
        }
    }

    @Override public void onItemLongClick(int position, View v, CommentsModel item) {
        if (item.getUser() != null && TextUtils.equals(item.getUser().getLogin(), UserModel.getUser().getLogin())) {
            if (getView() != null) getView().onShowDeleteMsg(item.getId());
        } else {
            onItemClick(position, v, item);
        }
    }
}
