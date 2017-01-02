package com.fastaccess.ui.modules.repo.code.commits;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repo.code.commits.view.CommitPagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class RepoCommitsPresenter extends BasePresenter<RepoCommitsMvp.View> implements RepoCommitsMvp.Presenter {

    private ArrayList<CommitModel> commits = new ArrayList<>();
    private String login;
    private String repoId;
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

    @Override public void onCallApi(int page, @Nullable Object parameter) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(RepoCommitsMvp.View::onHideProgress);
            return;
        }
        if (repoId == null || login == null) return;
        manageSubscription(RxHelper.getObserver(RestClient.getCommits(login, repoId, page))
                .doOnSubscribe(() -> sendToView(RepoCommitsMvp.View::onShowProgress))
                .doOnNext(response -> {
                    lastPage = response.getLast();
                    setApiCalled();
                    if (getCurrentPage() == 1) {
                        getCommits().clear();
                    }
                    getCommits().addAll(response.getItems());
                    sendToView(RepoCommitsMvp.View::onNotifyAdapter);
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
        if (!InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
            onCallApi(1, null);
        }
    }

    @NonNull @Override public ArrayList<CommitModel> getCommits() {
        return commits;
    }

    @Override public void onItemClick(int position, View v, CommitModel item) {
        CommitPagerView.createIntentForOffline(v.getContext(), item);
    }

    @Override public void onItemLongClick(int position, View v, CommitModel item) {
        onItemClick(position, v, item);
    }
}
