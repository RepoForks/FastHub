package com.fastaccess.ui.modules.main.home;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.data.dao.EventsModel;
import com.fastaccess.data.dao.types.EventsType;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repo.RepoPagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 11 Nov 2016, 12:36 PM
 */

public class HomePresenter extends BasePresenter<HomeMvp.View> implements HomeMvp.Presenter {
    private ArrayList<EventsModel> eventsModels = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;

    @Override public void onCallApi(int page) {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            sendToView(view -> view.getLoadMore().reset());
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            sendToView(HomeMvp.View::onHideProgress);
            return;
        }
        manageSubscription(
                RxHelper.getObserver(
                        RestClient.getReceivedEvents(page))
                        .doOnSubscribe(() -> sendToView(HomeMvp.View::onShowProgress))
                        .doOnNext(response -> {
                            lastPage = response.getLast();
                            if (getCurrentPage() == 1) {
                                eventsModels.clear();
                                manageSubscription(EventsModel.save(response.getItems()).subscribe());
                            }
                            eventsModels.addAll(response.getItems());
                            sendToView(HomeMvp.View::onNotifyAdapter);
                        })
                        .onErrorReturn(throwable -> {
                            throwable.printStackTrace();
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            onWorkOffline();
                            return null;
                        })
                        .subscribe());
    }

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
        onCallApi(page);
    }

    @NonNull @Override public ArrayList<EventsModel> getEvents() {
        return eventsModels;
    }

    @Override public void onWorkOffline() {
        if (eventsModels.isEmpty()) {
            manageSubscription(
                    EventsModel.getEvents()
                            .subscribe(eventsModels1 -> {
                                if (eventsModels1 != null && !eventsModels1.isEmpty()) {
                                    Logger.e(eventsModels1.size());
                                    eventsModels.addAll(eventsModels1);
                                    sendToView(HomeMvp.View::onNotifyAdapter);
                                }
                            })
            );
        }
    }

    @Override public void onItemClick(int position, View v, EventsModel item) {
        if (item.getType() == EventsType.ForkEvent) {
            RepoPagerView.startRepoPager(v.getContext(), item.getPayload().getForkee());
        } else {
            Uri uri = Uri.parse(item.getRepo().getName());
            if (uri.getPathSegments() != null && !uri.getPathSegments().isEmpty()) {
                Logger.e(uri.getPathSegments().get(0));
                String owner = uri.getPathSegments().get(0);
                String repoId = uri.getLastPathSegment();
                Intent intent = RepoPagerView.createIntent(v.getContext(), repoId, owner);
                v.getContext().startActivity(intent);

            }
        }
    }

    @Override public void onItemLongClick(int position, View v, EventsModel item) {
        //TODO
    }
}
