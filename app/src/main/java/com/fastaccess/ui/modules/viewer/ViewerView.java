package com.fastaccess.ui.modules.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.StateLayout;
import com.prettifier.pretty.PrettifyWebView;

import butterknife.BindView;

/**
 * Created by Kosh on 28 Nov 2016, 9:27 PM
 */

public class ViewerView extends BaseFragment<ViewerMvp.View, ViewerPresenter> implements ViewerMvp.View {

    public static final String TAG = ViewerView.class.getSimpleName();

    @BindView(R.id.textView) FontTextView textView;
    @BindView(R.id.textViewHolder) View textViewHolder;
    @BindView(R.id.webView) PrettifyWebView webView;
    @BindView(R.id.stateLayout) StateLayout stateLayout;

    public static ViewerView newInstance(@NonNull String repoId, @NonNull String login, @Nullable String repoUrl) {
        return newInstance(Bundler.start()
                .put(BundleConstant.EXTRA_ID, login)
                .put(BundleConstant.ID, repoId)
                .put(BundleConstant.EXTRA, repoUrl)
                .end());
    }

    public static ViewerView newInstance(@NonNull Bundle bundle) {
        ViewerView fragmentView = new ViewerView();
        fragmentView.setArguments(bundle);
        return fragmentView;
    }

    @Override public void onSetImageUrl(@NonNull String url) {
        webView.loadUrl(url);
        webView.setOnContentChangedListener(this);
        webView.setVisibility(View.VISIBLE);
    }

    @Override public void onSetMdText(@NonNull String text, String baseUrl) {
        stateLayout.hideProgress();
        webView.setVisibility(View.VISIBLE);
        if (getPresenter().isRepo()) {
            webView.setGithubContent(text, baseUrl);
        } else {
            webView.setMdSource(text);
        }
    }

    @Override public void onSetCode(@NonNull String text) {
        stateLayout.hideProgress();
        webView.setVisibility(View.VISIBLE);
        webView.setSource(text);
    }

    @Override public void onShowError(@NonNull String msg) {
        stateLayout.hideProgress();
        if (navigationCallback != null) {
            navigationCallback.showMessage(getString(R.string.error), msg);
        }
    }

    @Override public void onShowError(@StringRes int msg) {
        stateLayout.hideProgress();
        onShowError(getString(msg));
    }

    @Override public void onShowMdProgress() {
        stateLayout.showProgress();
    }

    @Override protected int fragmentLayout() {
        return R.layout.general_viewer_layout;
    }

    @NonNull @Override public ViewerPresenter providePresenter() {
        return new ViewerPresenter();
    }

    @Override public void onContentChanged(int progress) {
        if (progress == 100) {
            if (stateLayout != null) stateLayout.hideProgress();
        }
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (InputHelper.isEmpty(getPresenter().downloadedStream())) {
            getPresenter().onHandleIntent(getArguments());
        } else {
            if (getPresenter().isMarkDown()) {
                onSetMdText(getPresenter().downloadedStream(), getArguments().getString(BundleConstant.EXTRA));
            } else {
                onSetCode(getPresenter().downloadedStream());
            }
        }
    }
}
