package com.fastaccess.ui.modules.pull_request.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.adapter.PullRequestTimelineAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.AppbarRefreshLayout;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import butterknife.BindView;

/**
 * Created by Kosh on 13 Dec 2016, 12:40 AM
 */

public class PullRequestDetailsView extends BaseFragment<PullRequestDetailsMvp.View, PullRequestDetailsPresenter> implements PullRequestDetailsMvp
        .View {
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) AppbarRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;
    private PullRequestTimelineAdapter adapter;
    private OnLoadMore onLoadMore;

    public static PullRequestDetailsView newInstance(@NonNull PullRequestModel issueModel) {
        PullRequestDetailsView view = new PullRequestDetailsView();
        view.setArguments(Bundler.start().put(BundleConstant.ITEM, issueModel).end());
        return view;
    }

    @Override protected int fragmentLayout() {
        return R.layout.small_grid_refresh_list;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) getPresenter().onFragmentCreated(getArguments());
        recycler.setEmptyView(stateLayout, refresh);
        refresh.setOnRefreshListener(this);
        stateLayout.setOnReloadListener(this);
        adapter = new PullRequestTimelineAdapter(getPresenter().getEvents());
        adapter.setListener(getPresenter());
        getLoadMore().setCurrent_page(getPresenter().getCurrentPage(), getPresenter().getPreviousTotal());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        if (getPresenter().getEvents().size() == 1) {
            onRefresh();
        }
    }

    @NonNull @Override public PullRequestDetailsPresenter providePresenter() {
        return new PullRequestDetailsPresenter();
    }

    @Override public void onRefresh() {
        getPresenter().onCallApi(1, null);
    }

    @Override public void onNotifyAdapter() {
        onHideProgress();
        adapter.notifyDataSetChanged();
    }

    @Override public void onHideProgress() {
        if (navigationCallback != null) navigationCallback.hideProgress();
        refresh.setRefreshing(false);
        stateLayout.hideProgress();
    }

    @Override public void onShowProgress() {
        refresh.setRefreshing(true);
        stateLayout.showProgress();
    }

    @Override public void onShowMessage(@NonNull String message) {
        onHideProgress();
        stateLayout.showReload(adapter.getItemCount());
        if (navigationCallback != null) navigationCallback.showMessage(getString(R.string.error), message);
    }

    @NonNull @Override public OnLoadMore getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore(getPresenter());
        }
        return onLoadMore;
    }

    @Override public void onClick(View view) {

    }
}