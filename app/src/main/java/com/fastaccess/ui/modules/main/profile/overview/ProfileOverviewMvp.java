package com.fastaccess.ui.modules.main.profile.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 03 Dec 2016, 9:15 AM
 */

public interface ProfileOverviewMvp {

    interface View extends BaseMvp.FAView {
        void onInitViews(@Nullable UserModel userModel);

        void onShowProgress();

        void onShowMessage(String message);
    }

    interface Presenter extends BaseMvp.FAPresenter<View> {
        void onFragmentCreated(@Nullable Bundle bundle);

        void onWorkOffline(@NonNull String login);

        void onSendUserToView(@Nullable UserModel userModel);
    }
}
