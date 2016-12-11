package com.fastaccess.ui.modules.repo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 09 Dec 2016, 4:16 PM
 */

public interface RepoPagerMvp {

    interface View extends BaseMvp.FAView {

        void onFinishActivity();

        void onInitRepo();

        void onShowProgress();

        void onShowMessage(@NonNull String msg);

        void onRepoWatched(boolean isWatched);

        void onRepoStarred(boolean isStarred);

        void onRepoForked(boolean isForked);

        void onEnableDisableWatch(boolean isEnabled);

        void onEnableDisableStar(boolean isEnabled);

        void onEnableDisableFork(boolean isEnabled);

        void onChangeWatchedCount(boolean isWatched);

        void onChangeStarCount(boolean isStarred);

        void onChangeForkCount(boolean isForked);
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {
        void onActivityCreated(@Nullable Intent intent);

        @NonNull String repoId();

        @NonNull String login();

        @Nullable RepoModel getRepo();

        boolean isWatched();

        boolean isStarred();

        boolean isForked();

        void onWatch();

        void onStar();

        void onFork();

        void onCheckWatching();

        void onCheckStarring();

        void onWorkOffline();
    }
}
