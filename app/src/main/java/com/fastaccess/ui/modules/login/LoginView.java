package com.fastaccess.ui.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.main.MainView;
import com.fastaccess.ui.widgets.FontButton;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

/**
 * Created by Kosh on 09 Nov 2016, 9:46 PM
 */

public class LoginView extends BaseActivity<LoginMvp.View, LoginPresenter> implements LoginMvp.View {

    @BindView(R.id.login) FontButton login;
    @State String code;

    @Override protected int layout() {
        return R.layout.login_layout;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return true;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override protected boolean isSecured() {
        return true;
    }

    @Override public void onShowProgress() {
        showProgress(0);
    }

    @Override public void onHideProgress() {
        hideProgress();
    }

    @Override public void onShowMessage(@StringRes int resId) {
        onShowMessage(getString(resId));
    }

    @Override public void onShowMessage(@NonNull String msg) {
        hideProgress();
        Snackbar.make(login, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override public void onSuccessfullyLoggedIn() {
        hideProgress();
        startActivity(new Intent(this, MainView.class));
        finish();
    }

    @Override public void onHandleIntentResult(@Nullable Intent intent) {
        if (InputHelper.isEmpty(code)) {
            code = getPresenter().onResume(intent);
            Logger.e(code);
            if (!InputHelper.isEmpty(code)) {
                getPresenter().onGetToken(code);
            }
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onHandleIntentResult(getIntent());
        setTitle(R.string.login);
    }

    @OnClick(R.id.login) public void onClick() {
        code = null;
        getPresenter().onAuthorize(this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onHandleIntentResult(intent);
    }

    @NonNull @Override public LoginPresenter providePresenter() {
        return new LoginPresenter();
    }
}

