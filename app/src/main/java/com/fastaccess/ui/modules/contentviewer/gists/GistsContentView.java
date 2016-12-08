package com.fastaccess.ui.modules.contentviewer.gists;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.adapter.GistsPagerAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.contentviewer.gists.comments.GistCommentsView;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.QuickReturnFooterBehavior;
import com.fastaccess.ui.widgets.ViewPagerView;
import com.fastaccess.ui.widgets.dialog.MessageDialogView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 12 Nov 2016, 12:18 PM
 */

public class GistsContentView extends BaseActivity<GistsContentMvp.View, GistsContentPresenter>
        implements GistsContentMvp.View {

    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.size) FontTextView size;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.pager) ViewPagerView pager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.startGist) ForegroundImageView startGist;
    @BindView(R.id.forkGist) ForegroundImageView forkGist;

    @OnClick(R.id.fab) void onAddComment() {
        GistCommentsView view = (GistCommentsView) pager.getAdapter().instantiateItem(pager, 0);
        if (view != null) {
            view.onStartNewComment();
        }
    }

    @OnClick(R.id.title) void onTitleClick() {
        if (getPresenter().getGist() != null && !InputHelper.isEmpty(getPresenter().getGist().getDescription()))
            showMessage(getString(R.string.details), getPresenter().getGist().getDescription());
    }

    @OnClick({R.id.startGist, R.id.forkGist}) public void onGistActions(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.startGist:
                getPresenter().onStarGist();
                break;
            case R.id.forkGist:
                getPresenter().onForkGist();
                break;
        }
    }

    @Override protected int layout() {
        return R.layout.gists_content_activity;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return false;
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

    @NonNull @Override public GistsContentPresenter providePresenter() {
        return new GistsContentPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
        }
        GistsModel gistsModel = getPresenter().getGist();
        if (gistsModel == null) {
            finish();
            return;
        }
        String url = gistsModel.getOwner() != null ? gistsModel.getOwner().getAvatarUrl() :
                     gistsModel.getUser() != null ? gistsModel.getUser().getAvatarUrl() : "";
        String login = gistsModel.getOwner() != null ? gistsModel.getOwner().getLogin() :
                       gistsModel.getUser() != null ? gistsModel.getUser().getLogin() : "";
        avatarLayout.setUrl(url, login);
        title.setText(gistsModel.getDisplayTitle(false));
        date.setText(ParseDateFormat.getTimeAgo(gistsModel.getCreatedAt()));
        size.setText(Formatter.formatFileSize(this, gistsModel.getSize()));
        pager.setAdapter(new GistsPagerAdapter(getSupportFragmentManager(), gistsModel, this));
        tabs.setupWithViewPager(pager);
        QuickReturnFooterBehavior quickReturnFooterBehavior = (QuickReturnFooterBehavior)
                ((CoordinatorLayout.LayoutParams) fab.getLayoutParams()).getBehavior();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (quickReturnFooterBehavior != null) {
                    quickReturnFooterBehavior.setEnabled(position == 0);
                }
                hideShowFab();
            }
        });
        onGistForked(getPresenter().isForked());
        onGistStarred(getPresenter().isStarred());
        hideShowFab();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gist_menu, menu);
        menu.findItem(R.id.deleteGist).setVisible(getPresenter().isOwner());
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (getPresenter().getGist() != null) ActivityHelper.shareUrl(this, getPresenter().getGist().getHtmlUrl());
            return true;
        } else if (item.getItemId() == R.id.deleteGist) {
            MessageDialogView.newInstance(
                    getString(R.string.delete_gist), getString(R.string.confirm_message),
                    Bundler.start().put(BundleConstant.EXTRA, true).end())
                    .show(getSupportFragmentManager(), MessageDialogView.TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {
        super.onMessageDialogActionClicked(isOk, bundle);
        if (bundle != null) {
            boolean isDelete = bundle.getBoolean(BundleConstant.EXTRA) && isOk;
            if (isDelete) {
                getPresenter().onDeleteGist();
            }
        }
    }

    @Override public void onShowProgress() {
        showProgress(0);
    }

    @Override public void onShowMessage(String message) {
        hideProgress();
        showMessage(getString(R.string.error), message);
    }

    @Override public void onSuccessDeleted() {
        hideProgress();
        finish();
    }

    @Override public void onErrorDeleting() {
        hideProgress();
        showMessage(getString(R.string.error), getString(R.string.error_deleting_gist));
    }

    @Override public void onGistStarred(boolean isStarred) {
        startGist.tintDrawable(isStarred ? R.color.carrot : R.color.black);
        startGist.setEnabled(true);
    }

    @Override public void onGistForked(boolean isForked) {
        forkGist.tintDrawable(isForked ? R.color.carrot : R.color.black);
        forkGist.setEnabled(true);
    }

    private void hideShowFab() {
        if (pager.getCurrentItem() == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }
}
