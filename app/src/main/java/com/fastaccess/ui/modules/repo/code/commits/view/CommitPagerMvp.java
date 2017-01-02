package com.fastaccess.ui.modules.repo.code.commits.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 10 Dec 2016, 9:21 AM
 */

public interface CommitPagerMvp {

    interface View extends BaseMvp.FAView {
        void onShowProgress();

        void onHideProgress();

        void onShowMessage(String message);

        void onSetup();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        @Nullable CommitModel getCommit();

        void onActivityCreated(@Nullable Intent intent);

        void onWorkOffline(@NonNull String sha, @NonNull String repoId, @NonNull String login);

    }

}
