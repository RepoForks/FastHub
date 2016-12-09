package com.fastaccess.ui.modules.search.repos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class SearchReposPresenter extends BasePresenter<SearchReposMvp.View> implements SearchReposMvp.Presenter {

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
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(SearchReposMvp.View::onHideProgress);
            return;
        }
        if (parameter == null) {
            return;
        }
        manageSubscription(RxHelper.getObserver(RestClient.searchRepos(parameter, page))
                .doOnSubscribe(() -> sendToView(SearchReposMvp.View::onShowProgress))
                .doOnNext(repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    if (page == 1) {
                        getRepos().clear();
                    }
                    getRepos().addAll(repoModelPageable.getItems());
                    sendToView(SearchReposMvp.View::onNotifyAdapter);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @NonNull @Override public ArrayList<RepoModel> getRepos() {
        return repos;
    }

    @Override public void onItemClick(int position, View v, RepoModel item) {

    }

    @Override public void onItemLongClick(int position, View v, RepoModel item) {}
}
