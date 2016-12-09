package com.fastaccess.ui.modules.interceptor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 09 Dec 2016, 12:30 PM
 */

public interface InterceptorMvp {

    interface View extends BaseMvp.FAView {
        void onFinishActivity();

        void onUriReceived(@NonNull Uri uri);
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {
        void onCreate(@Nullable Intent intent);
    }
}
