package com.fastaccess.ui.modules.repo.code.releases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fastaccess.R;
import com.fastaccess.data.dao.ReleasesModel;
import com.fastaccess.data.dao.SimpleUrlsModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.provider.rest.implementation.OnLoadMore;
import com.fastaccess.ui.adapter.ReleasesAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.dialog.ListDialogView;
import com.fastaccess.ui.widgets.dialog.MessageDialogView;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Kosh on 03 Dec 2016, 3:56 PM
 */

public class RepoReleasesView extends BaseFragment<RepoReleasesMvp.View, RepoReleasesPresenter> implements RepoReleasesMvp.View {
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;
    private OnLoadMore onLoadMore;
    private ReleasesAdapter adapter;

    public static RepoReleasesView newInstance(@NonNull String repoId, @NonNull String login) {
        RepoReleasesView view = new RepoReleasesView();
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
        adapter = new ReleasesAdapter(getPresenter().getReleases());
        adapter.setListener(getPresenter());
        getLoadMore().setCurrent_page(getPresenter().getCurrentPage(), getPresenter().getPreviousTotal());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        if (savedInstanceState == null) {
            getPresenter().onFragmentCreated(getArguments());
        } else if (getPresenter().getReleases().isEmpty()) {
            onRefresh();
        }
    }

    @NonNull @Override public RepoReleasesPresenter providePresenter() {
        return new RepoReleasesPresenter();
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

    @Override public void onDownload(@NonNull ReleasesModel item) {
        ListDialogView<SimpleUrlsModel> dialogView = new ListDialogView<>();
        dialogView.initArguments(getString(R.string.releases),
                Stream.of(new SimpleUrlsModel(getString(R.string.download_as_zip), item.getZipBallUrl()),
                        new SimpleUrlsModel(getString(R.string.download_as_tar), item.getTarballUrl()))
                        .collect(Collectors.toCollection(ArrayList::new)));
        dialogView.show(getChildFragmentManager(), "ListDialogView");
    }

    @Override public void onShowDetails(@NonNull ReleasesModel item) {
        if (!InputHelper.isEmpty(item.getBody())) {
            MessageDialogView.newInstance(!InputHelper.isEmpty(item.getName()) ? item.getName() : item.getTagName(),
                    item.getBody(), true).show(getChildFragmentManager(), "MessageDialogView");
        } else {
            if (navigationCallback != null) navigationCallback.showMessage(R.string.empty, R.string.no_body);
        }
    }

    @Override public void onRefresh() {
        getPresenter().onCallApi(1, null);
    }

    @Override public void onClick(View view) {
        onRefresh();
    }

    @Override public void onItemSelected(SimpleUrlsModel item) {
        RestProvider.downloadFile(getContext(), item.getUrl());
    }
}
