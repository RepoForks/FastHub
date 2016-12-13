package com.prettifier.pretty.callback;

import android.webkit.JavascriptInterface;

import com.fastaccess.helper.Logger;
import com.prettifier.pretty.PrettifyWebView;

/**
 * Created by Kosh on 13 Dec 2016, 3:01 PM
 */

public class MarkDownInterceptorInterface {
    private PrettifyWebView prettifyWebView;

    public MarkDownInterceptorInterface(PrettifyWebView prettifyWebView) {
        this.prettifyWebView = prettifyWebView;
        Logger.e();
    }


    @JavascriptInterface public void startIntercept() {
        Logger.e();
        if (prettifyWebView != null) {
            prettifyWebView.setInterceptTouch(true);
        }
    }

    @JavascriptInterface public void stopIntercept() {
        Logger.e();
        if (prettifyWebView != null) {
            prettifyWebView.setInterceptTouch(false);
        }
    }

}
