package com.fastaccess.ui.modules.interceptor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.provider.intent.IntentsManager;
import com.fastaccess.ui.base.BaseActivity;

/**
 * Created by Kosh on 09 Dec 2016, 12:31 PM
 */

public class InterceptorView extends BaseActivity<InterceptorMvp.View, InterceptorPresenter> implements InterceptorMvp.View {

    @Override protected int layout() {
        return 0;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return false;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override protected boolean isSecured() {
        return false;
    }

    @NonNull @Override public InterceptorPresenter providePresenter() {
        return new InterceptorPresenter();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().onCreate(getIntent());
    }

    @Override public void onFinishActivity() {
        finish();
    }

    @Override public void onUriReceived(@NonNull Uri uri) {
        Intent intent = new IntentsManager(this).checkUri(uri);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
