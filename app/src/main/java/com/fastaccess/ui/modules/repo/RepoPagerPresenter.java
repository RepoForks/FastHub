package com.fastaccess.ui.modules.repo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

/**
 * Created by Kosh on 09 Dec 2016, 4:17 PM
 */

public class RepoPagerPresenter extends BasePresenter<RepoPagerMvp.View> implements RepoPagerMvp.Presenter {
    private String login;
    private String repoId;

    @Override public void onActivityCreated(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            login = bundle.getString(BundleConstant.EXTRA);
            repoId = bundle.getString(BundleConstant.EXTRA_ID);
            if (!InputHelper.isEmpty(login()) && !InputHelper.isEmpty(repoId())) {
                sendToView(RepoPagerMvp.View::onInitAdapter);
                return;
            }
        }
        sendToView(RepoPagerMvp.View::onFinishActivity);
    }

    @NonNull @Override public String repoId() {
        return repoId;
    }

    @NonNull @Override public String login() {
        return login;
    }
}
