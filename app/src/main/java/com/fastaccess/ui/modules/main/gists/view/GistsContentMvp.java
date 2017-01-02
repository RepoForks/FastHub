package com.fastaccess.ui.modules.main.gists.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 12 Nov 2016, 12:17 PM
 */

public interface GistsContentMvp {

    interface View extends BaseMvp.FAView {
        void onShowProgress();

        void onHideProgress();

        void onShowMessage(String message);

        void onSuccessDeleted();

        void onErrorDeleting();

        void onGistStarred(boolean isStarred);

        void onGistForked(boolean isForked);

        void onSetupDetails();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        @Nullable GistsModel getGist();

        void onActivityCreated(@Nullable Intent intent);

        void onDeleteGist();

        boolean isOwner();

        void onStarGist();

        void onForkGist();

        boolean isForked();

        boolean isStarred();

        void checkStarring(@NonNull String gistId);

        void onWorkOffline(@NonNull String gistId);
    }
}
