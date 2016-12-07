package com.prettifier.pretty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fastaccess.helper.ActivityHelper;
import com.prettifier.pretty.utils.SourceUtils;


public class PrettifyWebView extends NestedWebView {
    private String content;
    private OnContentChangedListener onContentChangedListener;

    public interface OnContentChangedListener {
        void onContentChanged(int progress);
    }

    public PrettifyWebView(Context context) {
        super(context);
        if (isInEditMode()) return;
        initView();
    }

    public PrettifyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PrettifyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled") private void initView() {
        if (isInEditMode()) return;
        setWebChromeClient(new ChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setWebViewClient(new WebClient());
        } else {
            setWebViewClient(new WebClientCompat());
        }
        WebSettings settings = getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    public void setOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }

    public void setSource(String source) {
        if (source != null && !(source.length() == 0)) {
            this.content = source;
            String page = SourceUtils.generateContent(source);
            post(() -> loadDataWithBaseURL("file:///android_asset/highlight/", page, "text/html", "utf-8", null));
        } else Log.e(getClass().getSimpleName(), "Source can't be null or empty.");
    }

    public void refresh() {
        if (content != null) {
            loadUrl("about:blank");
            setSource(content);
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            if (onContentChangedListener != null) onContentChangedListener.onContentChanged(progress);
        }
    }

    private class WebClient extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Activity activity = ActivityHelper.getActivity(view.getContext());
            if (activity != null) {
                ActivityHelper.startCustomTab(activity, request.getUrl());
            }
            return true;
        }
    }

    private class WebClientCompat extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Activity activity = ActivityHelper.getActivity(view.getContext());
            if (activity != null) {
                ActivityHelper.startCustomTab(activity, Uri.parse(url));
            }
            return true;
        }
    }


}
