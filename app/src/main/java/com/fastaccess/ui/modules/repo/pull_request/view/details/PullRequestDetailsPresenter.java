package com.fastaccess.ui.modules.repo.pull_request.view.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.IssueEventAdapterModel;
import com.fastaccess.data.dao.IssueEventModel;
import com.fastaccess.data.dao.PullRequestAdapterModel;
import com.fastaccess.data.dao.PullRequestModel;
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

public class PullRequestDetailsPresenter extends BasePresenter<PullRequestDetailsMvp.View> implements PullRequestDetailsMvp.Presenter {
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    private ArrayList<PullRequestAdapterModel> events = new ArrayList<>();
    private PullRequestModel pullRequest;

    @Override public void onFragmentCreated(@Nullable Bundle bundle) {
        if (bundle == null) throw new NullPointerException("Bundle is null?");
        pullRequest = bundle.getParcelable(BundleConstant.ITEM);
        if (events.isEmpty()) {
            events.add(0, new PullRequestAdapterModel(PullRequestAdapterModel.HEADER, pullRequest));
            sendToView(PullRequestDetailsMvp.View::onNotifyAdapter);
        }
    }

    @NonNull @Override public ArrayList<PullRequestAdapterModel> getEvents() {
        return events;
    }

    @Override public void onItemClick(int position, View v, PullRequestAdapterModel item) {
        Logger.e(item.getType());
        if (item.getType() == IssueEventAdapterModel.HEADER) {} else {
            IssueEventModel issueEventModel = item.getIssueEvent();
            if (issueEventModel.getCommitUrl() != null) {
                Activity activity = ActivityHelper.getActivity(v.getContext());
                if (activity != null) {
                    ActivityHelper.startCustomTab(activity, issueEventModel.getCommitUrl());
                }
            }
        }
    }

    @Override public void onItemLongClick(int position, View v, PullRequestAdapterModel item) {
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
            sendToView(PullRequestDetailsMvp.View::onHideProgress);
            return;
        }
        setCurrentPage(page);
        String login = pullRequest.getLogin();
        String repoID = pullRequest.getRepoId();
        int number = pullRequest.getNumber();
        manageSubscription(
                RxHelper.getObserver(RestClient.getIssueTimeline(login, repoID, number, page))
                        .doOnSubscribe(() -> sendToView(PullRequestDetailsMvp.View::onShowProgress))
                        .doOnNext(response -> {
                            lastPage = response.getLast();
                            setApiCalled();
                            if (getCurrentPage() == 1) {
                                getEvents().subList(1, getEvents().size()).clear();
                            }
                            getEvents().addAll(PullRequestAdapterModel.addEvents(response.getItems()));
                            sendToView(PullRequestDetailsMvp.View::onNotifyAdapter);
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
