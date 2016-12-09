package com.fastaccess.ui.modules.interceptor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 09 Dec 2016, 12:31 PM
 */

public class InterceptorPresenter extends BasePresenter<InterceptorMvp.View> implements InterceptorMvp.Presenter {

    @Override public void onCreate(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) {
            sendToView(InterceptorMvp.View::onFinishActivity);
            return;
        }
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            if (uri != null) {
                sendToView(view -> view.onUriReceived(uri));
            } else {
                sendToView(InterceptorMvp.View::onFinishActivity);
            }
        } else {
            sendToView(InterceptorMvp.View::onFinishActivity);
        }
    }

}
