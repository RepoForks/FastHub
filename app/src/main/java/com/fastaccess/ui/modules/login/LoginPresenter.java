package com.fastaccess.ui.modules.login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.BuildConfig;
import com.fastaccess.R;
import com.fastaccess.data.dao.AccessTokenModel;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.rest.RestClient;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.helper.RxHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import retrofit2.Response;

/**
 * Created by Kosh on 09 Nov 2016, 9:43 PM
 */

public class LoginPresenter extends BasePresenter<LoginMvp.View> implements LoginMvp.Presenter {

    @Nullable @Override public String onResume(@Nullable Intent intent) {
        if (intent == null) return null;
        Uri uri = intent.getData();
        Logger.e(uri, intent);
        if (uri != null && uri.toString().startsWith(BuildConfig.REDIRECT_URL)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                return code;
            } else if (uri.getQueryParameter("error") != null) {
                Logger.e(uri.getQueryParameter("error"));
                if (isAttached()) getView().onShowMessage(R.string.failed_login);
            }
        }
        return null;
    }

    @Override public void onAuthorize(@NonNull LoginView loginView) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .authority("github.com")
                .appendPath("login")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                .appendQueryParameter("redirect_uri", BuildConfig.REDIRECT_URL)
                .appendQueryParameter("scope", "user,repo,gist,delete_repo,notifications")
                .appendQueryParameter("state", BuildConfig.APPLICATION_ID)
                .build();
        loginView.startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

    @Override public void onGetToken(@NonNull String code) {
        manageSubscription(
                RxHelper.getObserver(RestClient.getAccessToken(code))
                        .doOnSubscribe(() -> sendToView(LoginMvp.View::onShowProgress))
                        .doOnNext(this::onTokenResponse)
                        .onErrorReturn(throwable -> {
                            sendToView(view -> view.onShowMessage(throwable.getMessage()));
                            return null;
                        })
                        .subscribe());
    }

    @Override public void onTokenResponse(@Nullable Response<AccessTokenModel> modelResponse) {
        if (modelResponse != null) {
            if (modelResponse.body() != null) {
                String token = modelResponse.body().getAccessToken();
                if (!InputHelper.isEmpty(token)) {
                    PrefGetter.setToken(token);
                    manageSubscription(
                            RxHelper.getObserver(RestClient.getUser())
                                    .doOnSubscribe(() -> sendToView(LoginMvp.View::onShowProgress))
                                    .doOnNext(this::onUserResponse)
                                    .onErrorReturn(throwable -> {
                                        sendToView(view -> view.onShowMessage(throwable.getMessage()));
                                        return null;
                                    })
                                    .subscribe());
                    return;
                }
            } else {
                if (getView() != null) getView().onShowMessage(modelResponse.message());
                return;
            }
        }
        if (getView() != null) getView().onShowMessage(R.string.failed_login);
    }

    @Override public void onUserResponse(@Nullable Response<UserModel> modelResponse) {
        if (modelResponse != null) {
            UserModel userModel = modelResponse.body();
            if (userModel != null) {
                userModel.save();
                Logger.e(userModel);
                if (getView() != null) getView().onSuccessfullyLoggedIn();
            } else {
                if (getView() != null) getView().onShowMessage(modelResponse.message());
                return;
            }
        }
        if (getView() != null) getView().onShowMessage(R.string.failed_login);
    }
}
