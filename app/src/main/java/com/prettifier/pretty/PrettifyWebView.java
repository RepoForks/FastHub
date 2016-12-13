package com.prettifier.pretty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.InputHelper;
import com.prettifier.pretty.callback.MarkDownInterceptorInterface;
import com.prettifier.pretty.helper.MarkDownHelper;
import com.prettifier.pretty.helper.PrettifyHelper;


public class PrettifyWebView extends NestedWebView {
    private String content;
    private OnContentChangedListener onContentChangedListener;
    private boolean interceptTouch;

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

    @Override public boolean onInterceptTouchEvent(MotionEvent p_event) {
        return true;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(interceptTouch);
        }
        return super.onTouchEvent(event);
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
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
    }

    public void setOnContentChangedListener(@NonNull OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }

    public void setSource(@NonNull String source) {
        WebSettings settings = getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (!InputHelper.isEmpty(source)) {
            this.content = source;
            String page = PrettifyHelper.generateContent(source);
            post(() -> loadDataWithBaseURL("file:///android_asset/highlight/", page, "text/html", "utf-8", null));
        } else Log.e(getClass().getSimpleName(), "Source can't be null or empty.");
    }

    public void setMdSource(@NonNull String source) {
        if (!InputHelper.isEmpty(source)) {
            addJavascriptInterface(new MarkDownInterceptorInterface(PrettifyWebView.this), "Android");
            this.content = source;
            String page = MarkDownHelper.generateContent(source);
            post(() -> loadDataWithBaseURL("file:///android_asset/md/", page, "text/html", "utf-8", null));
        }
    }

    public void refresh() {
        if (content != null) {
            loadUrl("about:blank");
            setSource(content);
        }
    }

    public void setInterceptTouch(boolean interceptTouch) {
        this.interceptTouch = interceptTouch;
    }

    private class ChromeClient extends WebChromeClient {
        @Override public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            if (onContentChangedListener != null) {
                onContentChangedListener.onContentChanged(progress);
            }
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
