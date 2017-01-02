package com.fastaccess.ui.modules.main.gists;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.main.gists.view.GistsContentView;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

public class GistsPresenter extends BasePresenter<GistsMvp.View> implements GistsMvp.Presenter {
    private ArrayList<GistsModel> gistsModels = new ArrayList<>();
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
        if (page > lastPage || lastPage == 0) {
            sendToView(GistsMvp.View::onHideProgress);
            return;
        }
        setCurrentPage(page);
        manageSubscription(
                RxHelper.getObserver(RestClient.getPublicGists(page))
                        .doOnSubscribe(() -> sendToView(GistsMvp.View::onShowProgress))
                        .doOnNext(listResponse -> {
                            lastPage = listResponse.getLast();
                            setApiCalled();
                            if (getCurrentPage() == 1) {
                                getGists().clear();
                                manageSubscription(GistsModel.save(listResponse.getItems()).subscribe());
                            }
                            getGists().addAll(listResponse.getItems());
                            sendToView(GistsMvp.View::onNotifyAdapter);
                        })
                        .onErrorReturn(throwable -> {
                            throwable.printStackTrace();
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline();
                            return null;
                        })
                        .subscribe());
    }

    @NonNull @Override public ArrayList<GistsModel> getGists() {
        return gistsModels;
    }

    @Override public void onWorkOffline() {
        if (gistsModels.isEmpty()) {
            manageSubscription(
                    GistsModel.getGists().subscribe(gists -> {
                        if (gists != null && !gists.isEmpty()) {
                            Logger.e(gists.size());
                            gistsModels.addAll(gists);
                            sendToView(GistsMvp.View::onNotifyAdapter);
                        }
                    })
            );
        }
    }

    @Override public void onItemClick(int position, View v, GistsModel item) {
        Intent intent = new Intent(v.getContext(), GistsContentView.class);
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ITEM, item)
                .end());
        v.getContext().startActivity(intent);
    }

    @Override public void onItemLongClick(int position, View v, GistsModel item) {}
}
