package com.fastaccess.ui.modules.issue.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.IssueEventAdapterModel;
import com.fastaccess.data.dao.IssueEventModel;
import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 13 Dec 2016, 12:38 AM
 */

public class IssueDetailsPresenter extends BasePresenter<IssueDetailsMvp.View> implements IssueDetailsMvp.Presenter {
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    private ArrayList<IssueEventAdapterModel> events = new ArrayList<>();
    private IssueModel issueModel;

    @Override public void onFragmentCreated(@Nullable Bundle bundle) {
        if (bundle == null) throw new NullPointerException("Bundle is null?");
        issueModel = bundle.getParcelable(BundleConstant.ITEM);
        if (events.isEmpty()) {
            events.add(0, new IssueEventAdapterModel(IssueEventAdapterModel.HEADER, issueModel));
            sendToView(IssueDetailsMvp.View::onNotifyAdapter);
        }
    }

    @NonNull @Override public ArrayList<IssueEventAdapterModel> getIssues() {
        return events;
    }

    @Override public void onItemClick(int position, View v, IssueEventAdapterModel item) {
        Logger.e(item.getType());
        if (item.getType() == IssueEventAdapterModel.HEADER) {

        } else {
            IssueEventModel issueEventModel = item.getIssueEvent();
            if (issueEventModel.getCommitUrl() != null) {
                Activity activity = ActivityHelper.getActivity(v.getContext());
                if (activity != null) {
                    ActivityHelper.startCustomTab(activity, issueEventModel.getCommitUrl());
                }
            }
        }
    }

    @Override public void onItemLongClick(int position, View v, IssueEventAdapterModel item) {
        //TODO
    }

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

    @Override public void onCallApi(int page, @Nullable Object parameter) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        if (page > lastPage || lastPage == 0) {
            sendToView(IssueDetailsMvp.View::onHideProgress);
            return;
        }
        setCurrentPage(page);
        String login = issueModel.getLogin();
        String repoID = issueModel.getRepoId();
        int number = issueModel.getNumber();
        manageSubscription(
                RxHelper.getObserver(RestClient.getIssueTimeline(login, repoID, number, page))
                        .doOnSubscribe(() -> sendToView(IssueDetailsMvp.View::onShowProgress))
                        .doOnNext(response -> {
                            lastPage = response.getLast();
                            setApiCalled();
                            if (getCurrentPage() == 1) {
                                getIssues().subList(1, getIssues().size()).clear();
                            }
                            getIssues().addAll(IssueEventAdapterModel.addEvents(response.getItems()));
                            sendToView(IssueDetailsMvp.View::onNotifyAdapter);
                        })
                        .onErrorReturn(throwable -> {
                            throwable.printStackTrace();
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            return null;
                        })
                        .subscribe()
        );
    }
}
