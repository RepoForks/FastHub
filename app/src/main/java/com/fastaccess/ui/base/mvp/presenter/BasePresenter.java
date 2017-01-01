package com.fastaccess.ui.base.mvp.presenter;

import android.support.annotation.Nullable;

import com.fastaccess.ui.base.mvp.BaseMvp;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.rx.RxTiPresenterSubscriptionHandler;

import rx.Subscription;

/**
 * Created by Kosh on 25 May 2016, 9:12 PM
 */

public class BasePresenter<V extends BaseMvp.FAView> extends TiPresenter<V> implements BaseMvp.FAPresenter<V> {
    private boolean apiCalled;

    private RxTiPresenterSubscriptionHandler subscriptionHandler = new RxTiPresenterSubscriptionHandler(this);

    @Override public boolean isAttached() {
        return isViewAttached();
    }

    @Nullable @Override public V getView() {
        return super.getView();
    }

    @Override public void manageSubscription(@Nullable Subscription subscription) {
        if (subscription != null) {
            subscriptionHandler.manageSubscription(subscription);
        }
    }

    @Override public boolean isApiCalled() {
        return apiCalled;
    }

    @Override public void setApiCalled() {
        apiCalled = true;
    }
}
