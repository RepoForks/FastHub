package com.fastaccess.ui.modules.contentviewer.gists.files;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.adapter.FilesAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.modules.viewer.FilesViewerView;
import com.fastaccess.ui.widgets.StateLayout;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Kosh on 13 Nov 2016, 1:36 PM
 */

public class GistFilesListView extends BaseFragment<GistFilesListMvp.View, GistFilesListPresenter> implements
        GistFilesListMvp.View {

    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout) StateLayout stateLayout;

    public static GistFilesListView newInstance(@NonNull GistsModel gistsModel) {
        GistFilesListView view = new GistFilesListView();
        view.setArguments(Bundler.start().put(BundleConstant.ITEM, gistsModel).end());
        return view;
    }

    @Override protected int fragmentLayout() {
        return R.layout.small_grid_refresh_list;
    }

    @NonNull @Override public GistFilesListPresenter providePresenter() {
        return new GistFilesListPresenter();
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GistsModel gistsModel = getArguments().getParcelable(BundleConstant.ITEM);
        stateLayout.hideReload();
        stateLayout.setEmptyText(R.string.no_files);
        recycler.setEmptyView(stateLayout);
        refresh.setEnabled(false);
        if (gistsModel == null) {
            return;
        }
        if (gistsModel.getFiles() != null) {
            recycler.setAdapter(new FilesAdapter(new ArrayList<>(gistsModel.getFiles().values()), getPresenter()));
        }
    }

    @Override public void onOpenFile(@NonNull FilesListModel item) {
        GistsModel gistsModel = getArguments().getParcelable(BundleConstant.ITEM);
        Intent intent = new Intent(getContext(), FilesViewerView.class);
        if (gistsModel != null) {
            item.setId(gistsModel.getGistId());
            intent.putExtras(Bundler.start()
                    .put(BundleConstant.EXTRA, (Parcelable) item)
                    .end());
        }
        startActivity(intent);
    }
}
