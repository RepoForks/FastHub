package com.fastaccess.ui.modules.contentviewer.gists;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 12 Nov 2016, 12:17 PM
 */

public interface GistsContentMvp {

    interface View extends BaseMvp.FAView {
        void onShowProgress();

        void onShowMessage(String message);

        void onSuccessDeleted();

        void onErrorDeleting();

        void onGistStarred(boolean isStarred);

        void onGistForked(boolean isForked);
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
    }
}
