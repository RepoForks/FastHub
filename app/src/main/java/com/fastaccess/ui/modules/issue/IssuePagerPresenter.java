package com.fastaccess.ui.modules.issue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.App;
import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.dao.IssueRequestModel;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class IssuePagerPresenter extends BasePresenter<IssuePagerMvp.View> implements IssuePagerMvp.Presenter {
    private IssueModel issueModel;

    @Nullable @Override public IssueModel getIssue() {
        return issueModel;
    }

    @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            issueModel = intent.getExtras().getParcelable(BundleConstant.ITEM);
            long issueNumber = intent.getExtras().getInt(BundleConstant.ID);
            String login = intent.getExtras().getString(BundleConstant.EXTRA_ID);
            String repoId = intent.getExtras().getString(BundleConstant.EXTRA2_ID);
            if (issueModel != null) {
                sendToView(IssuePagerMvp.View::onSetupIssue);
                return;
            } else if (issueNumber > 0 && !InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
                manageSubscription(RxHelper.getObserver(RestClient.getIssue(login, repoId, issueNumber))
                        .doOnSubscribe(() -> sendToView(IssuePagerMvp.View::onShowProgress))
                        .doOnNext(issue -> {
                            issueModel = issue;
                            issueModel.setRepoId(repoId);
                            issueModel.setLogin(login);
                            sendToView(IssuePagerMvp.View::onSetupIssue);
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
        sendToView(IssuePagerMvp.View::onSetupIssue);
    }

    @Override public void onWorkOffline(long issueNumber, @NonNull String repoId, @NonNull String login) {
//TODO
    }

    @Override public boolean isOwner() {
        return getIssue() != null && getIssue().getUser() != null &&
                getIssue().getUser().getLogin().equals(App.getUser().getLogin());
    }

    @Override public boolean isLocked() {
        return getIssue() != null && getIssue().isLocked();
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
        IssueModel currentIssue = getIssue();
        if (currentIssue != null) {
            IssueRequestModel requestModel = IssueRequestModel.clone(currentIssue);
            manageSubscription(RxHelper.getObserver(RestClient.editIssue(currentIssue.getUser().getLogin(),
                    currentIssue.getRepoId(), currentIssue.getNumber(), requestModel))
                    .doOnSubscribe(() -> sendToView(IssuePagerMvp.View::onShowProgress))
                    .doOnNext(issue -> {
                        if (issue != null) {
                            sendToView(view -> view.showSuccessIssueActionMsg(currentIssue.getState() == IssueState.open));
                            issue.setRepoId(issueModel.getRepoId());
                            issue.setLogin(issueModel.getLogin());
                            issueModel = issue;
                            sendToView(IssuePagerMvp.View::onSetupIssue);
                        }
                    })
                    .onErrorReturn(throwable -> {
                        sendToView(view -> view.showErrorIssueActionMsg(currentIssue.getState() == IssueState.open));
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onLockUnlockIssue() {
        IssueModel currentIssue = getIssue();
        if (currentIssue == null) return;
        String login = currentIssue.getUser().getLogin();
        String repoId = currentIssue.getRepoId();
        int number = currentIssue.getNumber();
        Observable<Response<Boolean>> observable = RxHelper
                .getObserver(isLocked() ? RestClient.unLockIssue(login, repoId, number) : RestClient.lockIssue(login, repoId, number));
        manageSubscription(observable
                .doOnSubscribe(() -> sendToView(IssuePagerMvp.View::onShowProgress))
                .doOnNext(booleanResponse -> {
                    int code = booleanResponse.code();
                    if (code == 204) {
                        issueModel.setLocked(!isLocked());
                        sendToView(IssuePagerMvp.View::onSetupIssue);
                    }
                    sendToView(IssuePagerMvp.View::onHideProgress);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());

    }
}
