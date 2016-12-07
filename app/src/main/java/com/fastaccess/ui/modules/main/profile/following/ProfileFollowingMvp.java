package com.fastaccess.ui.modules.main.profile.following;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.fastaccess.data.dao.UserModel;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Kosh on 03 Dec 2016, 3:45 PM
 */

public interface ProfileFollowingMvp {

    interface View extends BaseMvp.FAView, SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener {
        void onNotifyAdapter();

        void onHideProgress();

        void onShowProgress();

        void onShowMessage(@NonNull String message);

        @NonNull OnLoadMore getLoadMore();
    }

    interface Presenter extends BaseMvp.FAPresenter<View>,
            BaseViewHolder.OnItemClickListener<UserModel>,
            BaseMvp.PaginationListener<String> {

        @NonNull ArrayList<UserModel> getUsers();

        void onWorkOffline(@NonNull String login);
    }
}
