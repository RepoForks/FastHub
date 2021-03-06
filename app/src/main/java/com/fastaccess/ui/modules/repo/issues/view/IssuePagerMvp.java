package com.fastaccess.ui.modules.repo.issues.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 10 Dec 2016, 9:21 AM
 */

public interface IssuePagerMvp {

    interface View extends BaseMvp.FAView {
        void onShowProgress();

        void onHideProgress();

        void onShowMessage(String message);

        void onSetupIssue();

        void showSuccessIssueActionMsg(boolean isClose);

        void showErrorIssueActionMsg(boolean isClose);
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {

        @Nullable IssueModel getIssue();

        void onActivityCreated(@Nullable Intent intent);

        void onWorkOffline(long issueNumber, @NonNull String repoId, @NonNull String login);

        boolean isOwner();

        boolean isLocked();

        void onHandleConfirmDialog(@Nullable Bundle bundle);

        void onOpenCloseIssue();

        void onLockUnlockIssue();
    }

}
