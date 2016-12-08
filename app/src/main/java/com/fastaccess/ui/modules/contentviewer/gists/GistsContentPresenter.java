package com.fastaccess.ui.modules.contentviewer.gists;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fastaccess.App;
import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Kosh on 12 Nov 2016, 12:17 PM
 */

public class GistsContentPresenter extends BasePresenter<GistsContentMvp.View> implements GistsContentMvp.Presenter {


    private boolean isGistStarred;
    private boolean isGistForked;

    private GistsModel gist;

    @Nullable @Override public GistsModel getGist() {
        return gist;
    }

    @SuppressWarnings("unchecked") @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        gist = bundle.getParcelable(BundleConstant.ITEM);
        if (gist != null) {
            manageSubscription(RxHelper.getObserver(RestClient.isGistStarred(gist.getGistId()))
                    .doOnNext(booleanResponse -> {
                        isGistStarred = booleanResponse.code() == 204;
                        sendToView(view -> view.onGistStarred(isGistStarred));
                    })
                    .onErrorReturn(throwable -> {
                        sendToView(view -> view.onShowMessage(throwable.getMessage()));
                        return null;
                    })
                    .subscribe());
        }
    }

    @Override public void onDeleteGist() {
        if (getGist() == null) return;
        manageSubscription(RxHelper.getObserver(RestClient.deleteGist(getGist().getGistId()))
                .doOnSubscribe(() -> sendToView(GistsContentMvp.View::onShowProgress))
                .doOnNext(booleanResponse -> {
                    if (booleanResponse.code() == 204) {
                        sendToView(GistsContentMvp.View::onSuccessDeleted);
                    } else {
                        sendToView(GistsContentMvp.View::onErrorDeleting);
                    }
                })
                .onErrorReturn(throwable -> {
                    sendToView(view -> view.onShowMessage(throwable.getMessage()));
                    return null;
                })
                .subscribe());
    }

    @Override public boolean isOwner() {
        return getGist() != null && getGist().getOwner() != null &&
                getGist().getOwner().getLogin().equals(App.getUser().getLogin());
    }

    @Override public void onStarGist() {
        if (getGist() != null) {
            Observable<Response<Boolean>> observable = RxHelper.getObserver(
                    !isGistStarred ? RestClient.starGist(getGist().getGistId())
                                   : RestClient.unStarGist(getGist().getGistId()))
                    .doOnNext(booleanResponse -> {
                        if (!isGistStarred) {
                            isGistStarred = booleanResponse.code() == 204;
                        } else {
                            isGistStarred = booleanResponse.code() != 204;
                        }
                        sendToView(view -> view.onGistStarred(isGistStarred));
                    })
                    .onErrorReturn(throwable -> {
                        sendToView(view -> view.onShowMessage(throwable.getMessage()));
                        return null;
                    });
            manageSubscription(observable.subscribe());
        }
    }

    @Override public void onForkGist() {
        if (getGist() != null) {
            if (!isGistForked) {
                manageSubscription(RxHelper.getObserver(RestClient.forkGist(getGist().getGistId()))
                        .doOnNext(booleanResponse -> {
                            isGistForked = booleanResponse.code() == 201;
                            sendToView(view -> view.onGistForked(isGistForked));
                        })
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            return null;
                        })
                        .subscribe());
            }
        }
    }

    @Override public boolean isForked() {
        return isGistForked;
    }

    @Override public boolean isStarred() {
        return isGistStarred;
    }
}
