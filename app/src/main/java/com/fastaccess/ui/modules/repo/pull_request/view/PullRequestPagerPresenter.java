package com.fastaccess.ui.modules.repo.pull_request.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.App;
import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

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
        return getPullRequest() != null && getPullRequest().getUser() != null &&
                getPullRequest().getUser().getLogin().equals(App.getUser().getLogin());
    }

    @Override public boolean isLocked() {
        return getPullRequest() != null && getPullRequest().isLocked();
    }

    @Override public void onHandleConfirmDialog(@Nullable Bundle bundle) {
        if (bundle != null) {
            boolean proceed = bundle.getBoolean(BundleConstant.EXTRA);
            boolean proceedLockUnlock = bundle.getBoolean(BundleConstant.EXTRA_ID);
            if (proceed) {
                onOpenCloseIssue();
            } else if (proceedLockUnlock) {
                onLockUnlockIssue();
            }
        }
    }

    @Override public void onOpenCloseIssue() {
//        IssueModel currentIssue = getIssue();
//        if (currentIssue != null) {
//            IssueRequestModel requestModel = IssueRequestModel.clone(currentIssue);
//            manageSubscription(RxHelper.getObserver(RestClient.editIssue(currentIssue.getUser().getLogin(),
//                    currentIssue.getRepoId(), currentIssue.getNumber(), requestModel))
//                    .doOnSubscribe(() -> sendToView(PullRequestPagerMvp.View::onShowProgress))
//                    .doOnNext(issue -> {
//                        if (issue != null) {
//                            sendToView(view -> view.showSuccessIssueActionMsg(currentIssue.getState() == IssueState.open));
//                            issue.setRepoId(pullRequest.getRepoId());
//                            issue.setLogin(pullRequest.getLogin());
//                            pullRequest = issue;
//                            sendToView(PullRequestPagerMvp.View::onSetupIssue);
//                        }
//                    })
//                    .onErrorReturn(throwable -> {
//                        sendToView(view -> view.showErrorIssueActionMsg(currentIssue.getState() == IssueState.open));
//                        return null;
//                    })
//                    .subscribe());
//        }
    }

    @Override public void onLockUnlockIssue() {
//        IssueModel currentIssue = getIssue();
//        if (currentIssue == null) return;
//        String login = currentIssue.getUser().getLogin();
//        String repoId = currentIssue.getRepoId();
//        int number = currentIssue.getNumber();
//        Observable<Response<Boolean>> observable = RxHelper
//                .getObserver(isLocked() ? RestClient.unLockIssue(login, repoId, number) : RestClient.lockIssue(login, repoId, number));
//        manageSubscription(observable
//                .doOnSubscribe(() -> sendToView(PullRequestPagerMvp.View::onShowProgress))
//                .doOnNext(booleanResponse -> {
//                    int code = booleanResponse.code();
//                    if (code == 204) {
//                        pullRequest.setLocked(!isLocked());
//                        sendToView(PullRequestPagerMvp.View::onSetupIssue);
//                    }
//                    sendToView(PullRequestPagerMvp.View::onHideProgress);
//                })
//                .onErrorReturn(throwable -> {
//                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
//                    return null;
//                })
//                .subscribe());

    }
}
