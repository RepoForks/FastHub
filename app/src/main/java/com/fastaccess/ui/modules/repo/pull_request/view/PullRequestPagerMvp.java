package com.fastaccess.ui.modules.repo.pull_request.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.SpannableBuilder;

/**
 * Created by Kosh on 10 Dec 2016, 9:21 AM
 */

public interface PullRequestPagerMvp {

    interface View extends BaseMvp.FAView {
        void onShowProgress();

        void onHideProgress();

        void onShowMessage(String message);

        void onSetupIssue();

        void showSuccessIssueActionMsg(boolean isClose);

        void showErrorIssueActionMsg(boolean isClose);
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        @Nullable PullRequestModel getPullRequest();

        void onActivityCreated(@Nullable Intent intent);

        void onWorkOffline(long issueNumber, @NonNull String repoId, @NonNull String login);

        boolean isOwner();

        boolean isRepoOwner();

        boolean isLocked();

        boolean isMergeable();

        void onHandleConfirmDialog(@Nullable Bundle bundle);

        void onLockUnlockConversations();

        @NonNull SpannableBuilder getMergeBy(@NonNull PullRequestModel pullRequest, @NonNull Context context);
    }

}
