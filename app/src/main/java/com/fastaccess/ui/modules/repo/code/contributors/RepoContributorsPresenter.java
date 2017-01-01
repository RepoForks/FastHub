package com.fastaccess.ui.modules.repo.code.contributors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class RepoContributorsPresenter extends BasePresenter<RepoContributorsMvp.View> implements RepoContributorsMvp.Presenter {

    private ArrayList<UserModel> users = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    private String repoId;
    private String login;

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
            sendToView(RepoContributorsMvp.View::onHideProgress);
            return;
        }
        manageSubscription(RxHelper.getObserver(RestClient.getContributors(login, repoId, page))
                .doOnSubscribe(() -> sendToView(RepoContributorsMvp.View::onShowProgress))
                .doOnNext(repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    setApiCalled();
                    if (getCurrentPage() == 1) {
                        getUsers().clear();
                    }
                    getUsers().addAll(repoModelPageable.getItems());
                    sendToView(RepoContributorsMvp.View::onNotifyAdapter);
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

    @NonNull @Override public ArrayList<UserModel> getUsers() {
        return users;
    }

    @Override public void onItemClick(int position, View v, UserModel item) {}

    @Override public void onItemLongClick(int position, View v, UserModel item) {}
}
