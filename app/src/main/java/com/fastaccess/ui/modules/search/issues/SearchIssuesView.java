package com.fastaccess.ui.modules.search.issues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.adapter.IssuesAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import butterknife.BindView;
import icepick.State;

/**
 * Created by Kosh on 03 Dec 2016, 3:56 PM
 */

public class SearchIssuesView extends BaseFragment<SearchIssuesMvp.View, SearchIssuesPresenter> implements SearchIssuesMvp.View {

    @State String searchQuery;
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;
    private OnLoadMore<String> onLoadMore;
    private IssuesAdapter adapter;

    public static SearchIssuesView newInstance() {
        return new SearchIssuesView();
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
        if (savedInstanceState == null) {
            stateLayout.hideProgress();
        }
        getLoadMore().setCurrent_page(getPresenter().getCurrentPage(), getPresenter().getPreviousTotal());
        stateLayout.setOnReloadListener(this);
        refresh.setOnRefreshListener(this);
        recycler.setEmptyView(stateLayout, refresh);
        adapter = new IssuesAdapter(getPresenter().getIssues());
        adapter.setListener(getPresenter());
        recycler.setAdapter(adapter);
    }

    @NonNull @Override public SearchIssuesPresenter providePresenter() {
        return new SearchIssuesPresenter();
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

    @Override public void onSetSearchQuery(@NonNull String query) {
        this.searchQuery = query;
        getLoadMore().reset();
        getPresenter().getIssues().clear();
        onNotifyAdapter();
        if (!InputHelper.isEmpty(query)) {
            recycler.removeOnScrollListener(getLoadMore());
            recycler.addOnScrollListener(getLoadMore());
            onRefresh();
        }
    }

    @NonNull @Override public OnLoadMore<String> getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore<>(getPresenter(), searchQuery);
        }
        onLoadMore.setParameter(searchQuery);
        return onLoadMore;
    }

    @Override public void onRefresh() {
        getPresenter().onCallApi(1, searchQuery);
    }

    @Override public void onClick(View view) {
        onRefresh();
    }
}