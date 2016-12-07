package com.fastaccess.ui.modules.user;

import android.support.annotation.NonNull;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 04 Dec 2016, 1:11 PM
 */

public interface UserPagerMvp {

    interface View extends BaseMvp.FAView {
        void onInvalidateMenuItem();
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {
        void onCheckFollowStatus(@NonNull String login);

        boolean isSuccessResponse();

        boolean isFollowing();

        void onFollowMenuItemClicked(@NonNull String login);
    }

}
