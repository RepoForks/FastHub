package com.fastaccess.ui.modules.contentviewer.gists;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 12 Nov 2016, 12:17 PM
 */

public class GistsContentPresenter extends BasePresenter<GistsContentMvp.View> implements GistsContentMvp.Presenter {


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
}
