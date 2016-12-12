package com.fastaccess.ui.modules.repo.issues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.adapter.IssuesAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import butterknife.BindView;

/**
 * Created by Kosh on 03 Dec 2016, 3:56 PM
 */

public class RepoIssuesView extends BaseFragment<RepoIssuesMvp.View, RepoIssuesPresenter> implements RepoIssuesMvp.View {
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;
    private OnLoadMore onLoadMore;
    private IssuesAdapter adapter;

    public static RepoIssuesView newInstance(@NonNull String repoId, @NonNull String login) {
        RepoIssuesView view = new RepoIssuesView();
        view.setArguments(Bundler.start()
                .put(BundleConstant.EXTRA_ID, login)
                .put(BundleConstant.ID, repoId)
                .end());
        return view;
    }

    @Override public void onNotifyAdapter() {
        Logger.e();
        onHideProgress();
        adapter.notifyDataSetChanged();
    }

    @Override protected int fragmentLayout() {
        return R.layout.small_grid_refresh_list;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            throw new NullPointerException("Bundle is null, therefore, issues can't be proceeded.");
        }
        stateLayout.setOnReloadListener(this);
        refresh.setOnRefreshListener(this);
        recycler.setEmptyView(stateLayout, refresh);
        adapter = new IssuesAdapter(getPresenter().getIssues());
        adapter.setListener(getPresenter());
        getLoadMore().setCurrent_page(getPresenter().getCurrentPage(), getPresenter().getPreviousTotal());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        if (savedInstanceState == null) {
            getPresenter().onFragmentCreated(getArguments());
        } else if (getPresenter().getIssues().isEmpty()) {
            onRefresh();
        }
    }

    @NonNull @Override public RepoIssuesPresenter providePresenter() {
        return new RepoIssuesPresenter();
    }

    @Override public void onHideProgress() {
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

    @Override public void onRefresh() {
        getPresenter().onCallApi(1, null);
    }

    @Override public void onClick(View view) {
        onRefresh();
    }
}