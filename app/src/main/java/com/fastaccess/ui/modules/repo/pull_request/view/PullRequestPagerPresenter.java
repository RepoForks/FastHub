package com.fastaccess.ui.modules.repo.pull_request.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.App;
import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.data.dao.PullsIssuesParser;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.widgets.SpannableBuilder;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class PullRequestPagerPresenter extends BasePresenter<PullRequestPagerMvp.View> implements PullRequestPagerMvp.Presenter {
    private PullRequestModel pullRequest;

    @Nullable @Override public PullRequestModel getPullRequest() {
        return pullRequest;
    }

    @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            pullRequest = intent.getExtras().getParcelable(BundleConstant.ITEM);
            int issueNumber = intent.getExtras().getInt(BundleConstant.ID);
            String login = intent.getExtras().getString(BundleConstant.EXTRA_ID);
            String repoId = intent.getExtras().getString(BundleConstant.EXTRA2_ID);
            if (pullRequest != null) {
                sendToView(PullRequestPagerMvp.View::onSetupIssue);
                return;
            } else if (issueNumber > 0 && !InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
                manageSubscription(RxHelper.getObserver(RestClient.getRepoPullRequest(login, repoId, issueNumber))
                        .doOnSubscribe(() -> sendToView(PullRequestPagerMvp.View::onShowProgress))
                        .doOnNext(pullRequestModelResponse -> {
                            pullRequest = pullRequestModelResponse;
                            pullRequest.setRepoId(repoId);
                            pullRequest.setLogin(login);
                            sendToView(PullRequestPagerMvp.View::onSetupIssue);
                        })
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline(issueNumber, repoId, login);
                            return null;
                        })
                        .subscribe());
                return;
            }
        }
        sendToView(PullRequestPagerMvp.View::onSetupIssue);
    }

    @Override public void onWorkOffline(long issueNumber, @NonNull String repoId, @NonNull String login) {
        //TODO
    }

    @Override public boolean isOwner() {
        boolean isOwner;
        if (getPullRequest() != null && getPullRequest().getAssignee() != null) {
            isOwner = getPullRequest().getAssignee().getLogin().equals(App.getUser().getLogin());
        } else {
            isOwner = getPullRequest().getBase() != null &&
                    getPullRequest().getBase().getUser().getLogin().equals(App.getUser().getLogin());
        }
        return isOwner;
    }

    @Override public boolean isRepoOwner() {
        return getPullRequest() != null && getPullRequest().getBase() != null &&
                getPullRequest().getBase().getUser().getLogin().equals(App.getUser().getLogin());
    }

    @Override public boolean isLocked() {
        return getPullRequest() != null && getPullRequest().isLocked();
    }

    @Override public boolean isMergeable() {
        return getPullRequest() != null && getPullRequest().isMergeable() && !getPullRequest().isMerged();
    }

    @Override public void onHandleConfirmDialog(@Nullable Bundle bundle) {
        if (bundle != null) {
            boolean proceedLockUnlock = bundle.getBoolean(BundleConstant.EXTRA_ID);
            if (proceedLockUnlock) {
                onLockUnlockConversations();
            }
        }
    }

    @Override public void onLockUnlockConversations() {
        PullRequestModel currentPullRequest = getPullRequest();
        if (currentPullRequest == null) return;
        PullsIssuesParser pullsIssuesParser = PullsIssuesParser.getForPullRequest(currentPullRequest.getHtmlUrl());
        if (pullsIssuesParser == null) return;
        String login = pullsIssuesParser.getLogin();
        String repoId = pullsIssuesParser.getRepoId();
        int number = currentPullRequest.getNumber();
        Observable<Response<Boolean>> observable = RxHelper
                .getObserver(isLocked() ? RestClient.unLockConversations(login, repoId, number) :
                             RestClient.lockConversations(login, repoId, number));
        manageSubscription(observable
                .doOnSubscribe(() -> sendToView(PullRequestPagerMvp.View::onShowProgress))
                .doOnNext(booleanResponse -> {
                    int code = booleanResponse.code();
                    if (code == 204) {
                        pullRequest.setLocked(!isLocked());
                        sendToView(PullRequestPagerMvp.View::onSetupIssue);
                    }
                    sendToView(PullRequestPagerMvp.View::onHideProgress);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());

    }

    @NonNull @Override public SpannableBuilder getMergeBy(@NonNull PullRequestModel pullRequest, @NonNull Context context) {
        return PullRequestModel.getMergeBy(pullRequest, context);
    }
}
