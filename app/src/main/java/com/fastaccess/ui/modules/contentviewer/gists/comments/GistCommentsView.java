package com.fastaccess.ui.modules.contentviewer.gists.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.adapter.CommentsAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.modules.comment.CommentsView;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.dialog.MessageDialogView;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import butterknife.BindView;
import retrofit2.Response;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

public class GistCommentsView extends BaseFragment<GistCommentsMvp.View, GistCommentsPresenter> implements GistCommentsMvp.View {

    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;

    private String gistId;
    private CommentsAdapter adapter;
    private OnLoadMore<String> onLoadMore;

    public static GistCommentsView newInstance(@NonNull String gistId) {
        GistCommentsView view = new GistCommentsView();
        view.setArguments(Bundler.start().put("gistId", gistId).end());
        return view;
    }

    @Override protected int fragmentLayout() {
        return R.layout.small_grid_refresh_list;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gistId = getArguments().getString("gistId");
        recycler.setEmptyView(stateLayout, refresh);
        if (gistId == null) return;
        refresh.setOnRefreshListener(this);
        stateLayout.setOnReloadListener(this);
        adapter = new CommentsAdapter(getPresenter().getComments());
        adapter.setListener(getPresenter());
        getLoadMore().setCurrent_page(getPresenter().getCurrentPage(), getPresenter().getPreviousTotal());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        if (getPresenter().getComments().isEmpty()) {
            onRefresh();
        }
    }

    @Override public void onRefresh() {
        getPresenter().onCallApi(1, gistId);
    }

    @Override public void onNotifyAdapter() {
        onHideProgress();
        adapter.notifyDataSetChanged();
    }

    @Override public void onHideProgress() {
        if (navigationCallback != null) navigationCallback.hideProgress();
        refresh.setRefreshing(false);
        stateLayout.hideProgress();
    }

    @Override public void onShowProgress() {
        refresh.setRefreshing(true);
        stateLayout.showProgress();
    }

    @Override public void onShowMessage(@NonNull String message) {
        onHideProgress();
        stateLayout.showReload(adapter.getItemCount());
        if (navigationCallback != null) navigationCallback.showMessage(getString(R.string.error), message);
    }

    @NonNull @Override public GistCommentsPresenter providePresenter() {
        return new GistCommentsPresenter();
    }

    @NonNull @Override public OnLoadMore<String> getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore<>(getPresenter(), gistId);
        }
        return onLoadMore;
    }

    @Override public void onEditComment(@NonNull CommentsModel item) {
        Intent intent = new Intent(getContext(), CommentsView.class);
        intent.putExtras(Bundler
                .start()
                .put(BundleConstant.ID, gistId)
                .put(BundleConstant.EXTRA, item.getBody())
                .put(BundleConstant.EXTRA_ID, item.getId())
                .put(BundleConstant.EXTRA_TYPE, BundleConstant.EDIT_COMMENT_EXTRA)
                .end());
        startActivityForResult(intent, BundleConstant.REQUEST_CODE);
    }

    @Override public void onStartNewComment() {
        Intent intent = new Intent(getContext(), CommentsView.class);
        intent.putExtras(Bundler
                .start()
                .put(BundleConstant.ID, gistId)
                .put(BundleConstant.EXTRA_TYPE, BundleConstant.NEW_COMMENT_EXTRA)
                .end());
        startActivityForResult(intent, BundleConstant.REQUEST_CODE);
    }

    @Override public void onHandleCommentDelete(@NonNull Response<Boolean> booleanResponse, long commId) {
        if (navigationCallback != null) navigationCallback.hideProgress();
        if (booleanResponse.code() == 204) {
            CommentsModel commentsModel = new CommentsModel();
            commentsModel.setId(commId);
            adapter.removeItem(commentsModel);
        } else {
            if (navigationCallback != null) navigationCallback.showMessage(getString(R.string.error), getString(R.string.error_deleting_comment));
        }
    }

    @Override public void onShowDeleteMsg(long id) {
        MessageDialogView.newInstance(getString(R.string.delete), getString(R.string.confirm_message),
                Bundler.start()
                        .put(BundleConstant.EXTRA, id)
                        .put(BundleConstant.ID, gistId)
                        .end())
                .show(getChildFragmentManager(), MessageDialogView.TAG);
    }

    @Override public void onShowProgressDialog() {
        if (navigationCallback != null) navigationCallback.showProgress(0);
    }

    @Override public void onTagUser(@NonNull UserModel user) {
        Intent intent = new Intent(getContext(), CommentsView.class);
        intent.putExtras(Bundler
                .start()
                .put(BundleConstant.ID, gistId)
                .put(BundleConstant.EXTRA, "@" + user.getLogin())
                .put(BundleConstant.EXTRA_TYPE, BundleConstant.NEW_COMMENT_EXTRA)
                .end());
        startActivityForResult(intent, BundleConstant.REQUEST_CODE);
    }

    @Override public void onDestroyView() {
        recycler.removeOnScrollListener(getLoadMore());
        super.onDestroyView();
    }

    @Override public void onClick(View view) {
        onRefresh();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data, recycler, adapter);
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {
        super.onMessageDialogActionClicked(isOk, bundle);
        if (isOk) {
            getPresenter().onHandleDeletion(bundle);
        }
    }
}
