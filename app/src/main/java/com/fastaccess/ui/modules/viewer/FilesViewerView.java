package com.fastaccess.ui.modules.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.fastaccess.R;
import com.fastaccess.data.dao.FilesListModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.FileHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;

import net.grandcentrix.thirtyinch.TiPresenter;

/**
 * Created by Kosh on 27 Nov 2016, 3:43 PM
 */

public class FilesViewerView extends BaseActivity {


    public static void startActivity(@NonNull Context context, @NonNull FilesListModel item) {
        Intent intent = new Intent(context, FilesViewerView.class);
        intent.putExtras(Bundler.start()
                .put(BundleConstant.EXTRA, (Parcelable) item)
                .end());
        context.startActivity(intent);
    }

    @Override protected int layout() {
        return R.layout.activity_fragment_layout;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return true;
    }

    @Override protected boolean isTransparent() {
        return false;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override protected boolean isSecured() {
        return false;
    }

    @NonNull @Override public TiPresenter providePresenter() {
        return new BasePresenter();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, ViewerView.newInstance(getIntent().getExtras()), ViewerView.TAG)
                    .commit();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.download) {

            if (getIntent().getExtras().get(BundleConstant.EXTRA) instanceof String) {
                String url = getIntent().getExtras().getString(BundleConstant.EXTRA);
                if (!InputHelper.isEmpty(url)) {
                    RestProvider.downloadFile(this, url);
                }
            } else {
                FilesListModel filesListModel = getIntent().getExtras().getParcelable(BundleConstant.EXTRA);
                if (filesListModel != null) {
                    RestProvider.downloadFile(this, filesListModel.getRawUrl(),
                            FileHelper.getExtension(filesListModel.getFilename())
                                    != null ? filesListModel.getFilename() : null);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
