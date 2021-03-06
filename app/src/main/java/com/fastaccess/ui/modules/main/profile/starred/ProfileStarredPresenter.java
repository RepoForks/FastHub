package com.fastaccess.ui.modules.main.profile.starred;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repo.RepoPagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class ProfileStarredPresenter extends BasePresenter<ProfileStarredMvp.View> implements ProfileStarredMvp.Presenter {

    private ArrayList<RepoModel> repos = new ArrayList<>();
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

    @Override public void onCallApi(int page, @Nullable String parameter) {
        if (parameter == null) {
            throw new NullPointerException("Username is null");
        }
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(ProfileStarredMvp.View::onHideProgress);
            return;
        }
        manageSubscription(RxHelper.getObserver(RestClient.getStarred(parameter, page))
                .doOnSubscribe(() -> sendToView(ProfileStarredMvp.View::onShowProgress))
                .doOnNext(repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    setApiCalled();
                    if (getCurrentPage() == 1) {
                        getRepos().clear();
                        manageSubscription(RepoModel.saveStarred(parameter, repoModelPageable.getItems()).subscribe());
                    }
                    getRepos().addAll(repoModelPageable.getItems());
                    sendToView(ProfileStarredMvp.View::onNotifyAdapter);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    onWorkOffline(parameter);
                    return null;
                })
                .subscribe());
    }

    @NonNull @Override public ArrayList<RepoModel> getRepos() {
        return repos;
    }

    @Override public void onWorkOffline(@NonNull String login) {
        if (repos.isEmpty()) {
            manageSubscription(RepoModel.getStarredRepos(login)
                    .subscribe(repoModels -> {
                        if (repoModels != null) {
                            repos.addAll(repoModels);
                            sendToView(ProfileStarredMvp.View::onNotifyAdapter);
                        }
                    }));
        }
    }

    @Override public void onItemClick(int position, View v, RepoModel item) {
        RepoPagerView.startRepoPager(v.getContext(), item);
    }

    @Override public void onItemLongClick(int position, View v, RepoModel item) {
        onItemClick(position, v, item);
    }
}
