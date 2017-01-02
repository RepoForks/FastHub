package com.fastaccess.ui.modules.repo.pull_request.view.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.fastaccess.data.dao.PullRequestAdapterModel;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Kosh on 13 Dec 2016, 12:36 AM
 */

public interface PullRequestDetailsMvp {

    interface View extends BaseMvp.FAView, SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener {
        void onNotifyAdapter();

        void onHideProgress();

        void onShowProgress();

        void onShowMessage(@NonNull String message);

        @NonNull OnLoadMore getLoadMore();
    }

    interface Presenter extends BaseMvp.FAPresenter<View>, BaseViewHolder.OnItemClickListener<PullRequestAdapterModel>,
            BaseMvp.PaginationListener {

        @NonNull ArrayList<PullRequestAdapterModel> getEvents();

        void onFragmentCreated(@Nullable Bundle bundle);
    }
}
