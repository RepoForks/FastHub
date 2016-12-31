package com.fastaccess.ui.modules.repo.pull_request.lists;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.pull_request.PullRequestPagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class RepoPullRequestPresenter extends BasePresenter<RepoPullRequestMvp.View> implements RepoPullRequestMvp.Presenter {

    private ArrayList<PullRequestModel> pullRequests = new ArrayList<>();
    private String login;
    private String repoId;
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    IssueState issueState;

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

    @Override public void onCallApi(int page, @Nullable IssueState parameter) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(RepoPullRequestMvp.View::onHideProgress);
            return;
        }
        if (repoId == null || login == null) return;
        manageSubscription(RxHelper.getObserver(RestClient.getRepoPullRequests(login, repoId, issueState, page))
                .doOnSubscribe(() -> sendToView(RepoPullRequestMvp.View::onShowProgress))
                .doOnNext(response -> {
                    lastPage = response.getLast();
                    if (page == 1) {
                        getPullRequests().clear();
                    }
                    getPullRequests().addAll(response.getItems());
                    sendToView(RepoPullRequestMvp.View::onNotifyAdapter);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @Override public void onFragmentCreated(@NonNull Bundle bundle) {
        login = bundle.getString(BundleConstant.EXTRA_ID);
        repoId = bundle.getString(BundleConstant.ID);
        issueState = (IssueState) bundle.getSerializable(BundleConstant.EXTRA);
        if (!InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
            onCallApi(1, null);
        }
    }

    @NonNull public ArrayList<PullRequestModel> getPullRequests() {
        return pullRequests;
    }

    @Override public void onItemClick(int position, View v, PullRequestModel item) {
        Logger.e(Bundler.start().put("item", item).end().size());
        PullRequestPagerView.createIntentForOffline(v.getContext(), item);
    }

    @Override public void onItemLongClick(int position, View v, PullRequestModel item) {
        onItemClick(position, v, item);
    }
}
