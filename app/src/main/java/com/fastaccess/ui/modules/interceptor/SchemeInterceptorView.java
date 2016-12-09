package com.fastaccess.ui.modules.interceptor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fastaccess.provider.scheme.SchemeParser;

/**
 * Created by Kosh on 09 Dec 2016, 12:31 PM
 */

public class SchemeInterceptorView extends Activity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(getIntent());
    }

    private void onCreate(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            finish();
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            onUriReceived();
        } else {
            finish();
        }
    }

    private void onUriReceived() {
        SchemeParser.launchUri(this, getIntent());
        finish();
    }
}
