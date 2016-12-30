package com.fastaccess.ui.modules.repo.issues.lists;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.issue.IssuePagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class RepoIssuesPresenter extends BasePresenter<RepoIssuesMvp.View> implements RepoIssuesMvp.Presenter {

    private ArrayList<IssueModel> issues = new ArrayList<>();
    private String login;
    private String repoId;
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    private IssueState issueState;

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
            sendToView(RepoIssuesMvp.View::onHideProgress);
            return;
        }
        if (repoId == null || login == null) return;
        manageSubscription(RxHelper.getObserver(RestClient.getRepoIssues(login, repoId, issueState, page))
                .doOnSubscribe(() -> sendToView(RepoIssuesMvp.View::onShowProgress))
                .doOnNext(issues -> {
                    lastPage = issues.getLast();
                    if (page == 1) {
                        getIssues().clear();
                    }
                    //filter issues only.
                    getIssues().addAll(Stream.of(issues.getItems()).filter(value -> value.getPullRequest() == null).collect(Collectors.toList()));
                    sendToView(RepoIssuesMvp.View::onNotifyAdapter);
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

    @NonNull @Override public ArrayList<IssueModel> getIssues() {
        return issues;
    }

    @Override public void onItemClick(int position, View v, IssueModel item) {
        Logger.e(Bundler.start().put("item", item).end().size());
        IssuePagerView.createIntentForOffline(v.getContext(), item);
    }

    @Override public void onItemLongClick(int position, View v, IssueModel item) {
        onItemClick(position, v, item);
    }
}
