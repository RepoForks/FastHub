package com.fastaccess.ui.modules.interceptor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.helper.Logger;
import com.fastaccess.provider.intent.IntentsManager;

/**
 * Created by Kosh on 09 Dec 2016, 12:31 PM
 */

public class InterceptorView extends Activity {

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
            Uri uri = intent.getData();
            if (uri != null) {
                onUriReceived(uri);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void onUriReceived(@NonNull Uri uri) {
        Logger.e(uri);
        Intent intent = new IntentsManager(this).checkUri(uri);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

}
