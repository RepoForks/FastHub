package com.fastaccess.ui.modules.login;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.fastaccess.data.dao.AccessTokenModel;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.AppbarRefreshLayout;

import retrofit2.Response;

/**
 * Created by Kosh on 09 Nov 2016, 9:41 PM
 */

public interface LoginMvp {

    interface View extends BaseMvp.FAView, AppbarRefreshLayout.OnRefreshListener {
        void onShowProgress();

        void onHideProgress();

        void onShowMessage(@StringRes int resId);

        void onShowMessage(@NonNull String msg);

        void onSuccessfullyLoggedIn();
    }

    interface Presenter extends BaseMvp.FAPresenter<LoginMvp.View> {

        @Nullable String getCode(@NonNull String url);

        @NonNull Uri getAuthorizationUrl();

        void onGetToken(@NonNull String code);

        void onTokenResponse(@Nullable Response<AccessTokenModel> response);

        void onUserResponse(@Nullable Response<UserModel> modelResponse);
    }
}
