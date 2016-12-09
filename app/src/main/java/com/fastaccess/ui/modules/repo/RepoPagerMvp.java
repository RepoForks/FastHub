package com.fastaccess.ui.modules.repo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 09 Dec 2016, 4:16 PM
 */

public interface RepoPagerMvp {

    interface View extends BaseMvp.FAView {

        void onFinishActivity();

        void onInitAdapter();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {
        void onActivityCreated(@Nullable Intent intent);

        @NonNull String repoId();

        @NonNull String login();
    }
}
