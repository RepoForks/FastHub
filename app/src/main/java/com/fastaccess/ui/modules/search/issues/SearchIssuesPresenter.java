package com.fastaccess.ui.modules.search.issues;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.issue.IssuePagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class SearchIssuesPresenter extends BasePresenter<SearchIssuesMvp.View> implements SearchIssuesMvp.Presenter {

    private ArrayList<IssueModel> issues = new ArrayList<>();
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
            sendToView(SearchIssuesMvp.View::onHideProgress);
            return;
        }
        if (parameter == null) {
            return;
        }
        manageSubscription(RxHelper.getObserver(RestClient.searchIssues(parameter, page))
                .doOnSubscribe(() -> sendToView(SearchIssuesMvp.View::onShowProgress))
                .doOnNext(repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    if (page == 1) {
                        getIssues().clear();
                    }
                    getIssues().addAll(repoModelPageable.getItems());
                    sendToView(SearchIssuesMvp.View::onNotifyAdapter);
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @NonNull @Override public ArrayList<IssueModel> getIssues() {
        return issues;
    }

    @Override public void onItemClick(int position, View v, IssueModel item) {
        Logger.e(Bundler.start().put("item",item).end().size());
        IssuePagerView.createIntentForOffline(v.getContext(), item);
    }

    @Override public void onItemLongClick(int position, View v, IssueModel item) {
        onItemClick(position, v, item);
    }
}
