package com.fastaccess.ui.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fastaccess.R;
import com.fastaccess.helper.Logger;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.main.MainView;
import com.fastaccess.ui.widgets.AppbarRefreshLayout;
import com.prettifier.pretty.NestedWebView;

import butterknife.BindView;

/**
 * Created by Kosh on 09 Nov 2016, 9:46 PM
 */

public class LoginView extends BaseActivity<LoginMvp.View, LoginPresenter> implements LoginMvp.View {

    @BindView(R.id.webView) NestedWebView webView;
    @BindView(R.id.refresh) AppbarRefreshLayout refresh;

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
        Snackbar.make(webView, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override public void onSuccessfullyLoggedIn() {
        hideProgress();
        startActivity(new Intent(this, MainView.class));
        finish();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh.setOnRefreshListener(this);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress == 100) {
                    refresh.setRefreshing(false);
                } else if (progress < 100) {
                    refresh.setRefreshing(true);//if (!refresh.isRefreshing())  is handled by the method,we shouldn't care.
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String code = getPresenter().getCode(url);
                Logger.e(code, url);
                if (code != null) {
                    getPresenter().onGetToken(code);
                }
                return false;
            }
        });
        webView.loadUrl(getPresenter().getAuthorizationUrl().toString());
    }

    @NonNull @Override public LoginPresenter providePresenter() {
        return new LoginPresenter();
    }

    @Override public void onRefresh() {
        webView.loadUrl(getPresenter().getAuthorizationUrl().toString());
    }
}

