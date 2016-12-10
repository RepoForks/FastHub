package com.fastaccess.ui.modules.issues;

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

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class IssueDetailsPresenter extends BasePresenter<IssuesDetailsMvp.View> implements IssuesDetailsMvp.Presenter {
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
                sendToView(IssuesDetailsMvp.View::onSetupIssue);
                return;
            } else if (issueNumber > 0 && !InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
                manageSubscription(RxHelper.getObserver(RestClient.getIssue(login, repoId, issueNumber))
                        .doOnSubscribe(() -> sendToView(IssuesDetailsMvp.View::onShowProgress))
                        .doOnNext(issue -> {
                            issueModel = issue;
                            issueModel.setRepoId(repoId);
                            sendToView(IssuesDetailsMvp.View::onSetupIssue);
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
        sendToView(IssuesDetailsMvp.View::onSetupIssue);
    }

    @Override public void onWorkOffline(long issueNumber, @NonNull String repoId, @NonNull String login) {

    }

    @Override public boolean isOwner() {
        return getIssue() != null && getIssue().getUser() != null &&
                getIssue().getUser().getLogin().equals(App.getUser().getLogin());
    }

    @Override public void onOpenCloseIssue(@Nullable Bundle bundle) {
        if (bundle != null) {
            boolean proceed = bundle.getBoolean(BundleConstant.EXTRA);
            if (proceed) {
                IssueModel copyIssue = getIssue();
                if (copyIssue != null) {
                    IssueRequestModel requestModel = IssueRequestModel.clone(copyIssue);
                    manageSubscription(RxHelper.getObserver(RestClient.editIssue(copyIssue.getUser().getLogin(),
                            copyIssue.getRepoId(), copyIssue.getNumber(), requestModel))
                            .doOnSubscribe(() -> sendToView(IssuesDetailsMvp.View::onShowProgress))
                            .doOnNext(issue -> {
                                if (issue != null) {
                                    sendToView(view -> view.showSuccessIssueActionMsg(copyIssue.getState() == IssueState.open));
                                    issue.setRepoId(issueModel.getRepoId());
                                    issueModel = issue;
                                    sendToView(IssuesDetailsMvp.View::onSetupIssue);
                                }
                            })
                            .onErrorReturn(throwable -> {
                                sendToView(view -> view.showErrorIssueActionMsg(copyIssue.getState() == IssueState.open));

                                return null;
                            })
                            .subscribe());
                }
            }
        }
    }
}
