package com.fastaccess.ui.modules.repo.code.commits.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.fastaccess.R;
import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.data.dao.FragmentPagerAdapterModel;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.adapter.FragmentsPagerAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.repo.issues.view.comments.IssueCommentsView;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ViewPagerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class CommitPagerView extends BaseActivity<CommitPagerMvp.View, CommitPagerPresenter> implements CommitPagerMvp.View {

    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.headerTitle) FontTextView title;
    @BindView(R.id.size) FontTextView size;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.changes) FontTextView changes;
    @BindView(R.id.addition) FontTextView addition;
    @BindView(R.id.deletion) FontTextView deletion;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login, @NonNull String sha) {
        Intent intent = new Intent(context, CommitPagerView.class);
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ID, sha)
                .put(BundleConstant.EXTRA_ID, login)
                .put(BundleConstant.EXTRA2_ID, repoId)
                .end());
        return intent;

    }

    public static void createIntentForOffline(@NonNull Context context, @NonNull CommitModel commitModel) {
        SchemeParser.launchUri(context, Uri.parse(commitModel.getHtmlUrl()));
    }

    @OnClick(R.id.headerTitle) void onTitleClick() {
        if (getPresenter().getCommit() != null && !InputHelper.isEmpty(getPresenter().getCommit().getCommit().getMessage()))
            showMessage(getString(R.string.details), getPresenter().getCommit().getCommit().getMessage());
    }

    @OnClick(R.id.fab) void onAddComment() {
        IssueCommentsView view = (IssueCommentsView) pager.getAdapter().instantiateItem(pager, 1);
        if (view != null) {
            view.onStartNewComment();
        }
    }

    @Override protected int layout() {
        return R.layout.commit_pager_activity;
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

    @NonNull @Override public CommitPagerPresenter providePresenter() {
        return new CommitPagerPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
        } else {
            onSetup();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (getPresenter().getCommit() != null) ActivityHelper.shareUrl(this, getPresenter().getCommit().getHtmlUrl());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onShowProgress() {
        showProgress(0);
    }

    @Override public void onHideProgress() {
        hideProgress();
    }

    @Override public void onShowMessage(String message) {
        hideProgress();
        showMessage(getString(R.string.error), message);
    }

    @Override public void onSetup() {
        hideProgress();
        if (getPresenter().getCommit() == null) {
            finish();
            return;
        }
        supportInvalidateOptionsMenu();
        CommitModel commit = getPresenter().getCommit();
        String login = commit.getAuthor() != null ? commit.getAuthor().getLogin() : commit.getCommit().getAuthor().getName();
        String avatar = commit.getAuthor() != null ? commit.getAuthor().getAvatarUrl() : null;
        String dateValue = commit.getCommit().getAuthor().getDate();
        title.setText(commit.getCommit().getMessage());
        size.setText(ParseDateFormat.getTimeAgo(dateValue));
        avatarLayout.setUrl(avatar, login);
        addition.setText(String.valueOf(commit.getStats() != null ? commit.getStats().getAdditions() : 0));
        deletion.setText(String.valueOf(commit.getStats() != null ? commit.getStats().getDeletions() : 0));
        changes.setText(String.valueOf(commit.getFiles() != null ? commit.getFiles().size() : 0));
        pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapterModel.buildForCommit(this, commit)));
        tabs.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                hideShowFab();
            }
        });
        hideShowFab();
    }

    private void hideShowFab() {
        if (pager.getCurrentItem() == 1) {
            fab.show();
        } else {
            fab.hide();
        }
    }
}
