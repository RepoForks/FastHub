package com.fastaccess.ui.modules.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.FontTextView;
import com.prettifier.pretty.PrettifyWebView;

import butterknife.BindView;

/**
 * Created by Kosh on 28 Nov 2016, 9:27 PM
 */

public class ViewerView extends BaseFragment<ViewerMvp.View, ViewerPresenter> implements ViewerMvp.View {

    public static final String TAG = ViewerView.class.getSimpleName();

    @BindView(R.id.textView) FontTextView textView;
    @BindView(R.id.textViewHolder) ScrollView textViewHolder;
    @BindView(R.id.webView) PrettifyWebView webView;
    @BindView(R.id.progressBar) View progressBar;
    @BindView(R.id.webViewHolder) FrameLayout webViewHolder;


    public static ViewerView newInstance(@NonNull Bundle bundle) {
        ViewerView fragmentView = new ViewerView();
        fragmentView.setArguments(bundle);
        return fragmentView;
    }

    @Override public void onSetNormalText(@NonNull String text) {
        textViewHolder.setVisibility(View.VISIBLE);
        textView.setText(text);
    }

    @Override public void onSetMdText(@NonNull String text) {
        progressBar.setVisibility(View.GONE);
        textViewHolder.setVisibility(View.VISIBLE);
        MarkDownProvider.convertTextToMarkDown(textView, text);
    }

    @Override public void onSetCode(@NonNull String text) {
        webView.setOnContentChangedListener(this);
        webView.setSource(text);
        webViewHolder.setVisibility(View.VISIBLE);
    }

    @Override public void onShowError(@NonNull String msg) {
        if (navigationCallback != null) {
            navigationCallback.showMessage(getString(R.string.error), msg);
        }
    }

    @Override public void onShowError(@StringRes int msg) {
        onShowError(getString(msg));
    }

    @Override public void onShowMdProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override protected int fragmentLayout() {
        return R.layout.general_viewer_layout;
    }

    @NonNull @Override public ViewerPresenter providePresenter() {
        return new ViewerPresenter();
    }

    @Override public void onContentChanged(int progress) {
        if (progress == 100) {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (InputHelper.isEmpty(getPresenter().downloadedStream())) {
            getPresenter().onHandleIntent(getArguments());
        } else {
            if (getPresenter().isMarkDown()) {
                onSetMdText(getPresenter().downloadedStream());
            } else {
                onSetCode(getPresenter().downloadedStream());
            }
        }
    }
}
