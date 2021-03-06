package com.fastaccess.ui.modules.repo.code.releases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.ReleasesModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:48 PM
 */

public class RepoReleasesPresenter extends BasePresenter<RepoReleasesMvp.View> implements RepoReleasesMvp.Presenter {

    private ArrayList<ReleasesModel> releases = new ArrayList<>();
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
            sendToView(RepoReleasesMvp.View::onHideProgress);
            return;
        }
        if (repoId == null || login == null) return;
        manageSubscription(RxHelper.getObserver(RestClient.getReleases(login, repoId, page))
                .doOnSubscribe(() -> sendToView(RepoReleasesMvp.View::onShowProgress))
                .doOnNext(response -> {
                    lastPage = response.getLast();
                    setApiCalled();
                    if (getCurrentPage() == 1) {
                        getReleases().clear();
                    }
                    getReleases().addAll(response.getItems());
                    sendToView(RepoReleasesMvp.View::onNotifyAdapter);
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

    @NonNull @Override public ArrayList<ReleasesModel> getReleases() {
        return releases;
    }

    @Override public void onItemClick(int position, View v, ReleasesModel item) {
        if (getView() == null) return;
        if (v.getId() == R.id.download) {
            getView().onDownload(item);
        } else {
            getView().onShowDetails(item);
        }
    }

    @Override public void onItemLongClick(int position, View v, ReleasesModel item) {
        onItemClick(position, v, item);
    }
}
