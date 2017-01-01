package com.fastaccess.ui.modules.repo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.CountBadgeProvider;
import com.fastaccess.ui.widgets.FontTextView;

import java.text.NumberFormat;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by Kosh on 09 Dec 2016, 4:17 PM
 */

public class RepoPagerView extends BaseActivity<RepoPagerMvp.View, RepoPagerPresenter> implements RepoPagerMvp.View {

    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.headerTitle) FontTextView title;
    @BindView(R.id.size) FontTextView size;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.forkRepo) FontTextView forkRepo;
    @BindView(R.id.starRepo) FontTextView starRepo;
    @BindView(R.id.watchRepo) FontTextView watchRepo;
    @BindView(R.id.license) FontTextView license;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindColor(R.color.carrot) int carrotColor;
    @BindView(R.id.bottomNavigation) BottomNavigation bottomNavigation;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static void startRepoPager(@NonNull Context context, @NonNull RepoModel repoModel) {
        Intent intent = new Intent(context, RepoPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.ITEM, repoModel).end());
        context.startActivity(createIntent(context, repoModel.getName(), repoModel.getOwner().getLogin()));
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login) {
        Intent intent = new Intent(context, RepoPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA_ID, login).put(BundleConstant.ID, repoId).end());
        return intent;
    }

    @OnClick(R.id.headerTitle) void onTitleClick() {
        if (getPresenter().getRepo() != null && !InputHelper.isEmpty(getPresenter().getRepo().getDescription()))
            showMessage(getString(R.string.details), getPresenter().getRepo().getDescription());
    }

    @OnClick({R.id.forkRepo, R.id.starRepo, R.id.watchRepo}) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forkRepo:
                getPresenter().onFork();
                break;
            case R.id.starRepo:
                getPresenter().onStar();
                break;
            case R.id.watchRepo:
                getPresenter().onWatch();
                break;
        }
    }

    @Override protected int layout() {
        return R.layout.repo_pager_activity;
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

    @NonNull @Override public RepoPagerPresenter providePresenter() {
        return new RepoPagerPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
            bottomNavigation.setDefaultSelectedIndex(0);
        } else {
            if (getPresenter().getRepo() != null) {
                onInitRepo();
            }
        }
        setTitle("");//notitle....
    }

    @Override public void onNavigationChanged(@RepoPagerMvp.RepoNavigationType int navType) {
        //noinspection WrongConstant
        if (bottomNavigation.getSelectedIndex() != navType) bottomNavigation.setSelectedIndex(navType, true);

        getPresenter().onModuleChanged(getSupportFragmentManager(), navType);
    }

    @Override public void onFinishActivity() {
        finish();
    }

    @Override public void onInitRepo() {
        if (getPresenter().getRepo() == null) {
            finish();
            return;
        }
        bottomNavigation.setOnMenuItemClickListener(getPresenter());
        RepoModel repoModel = getPresenter().getRepo();
        hideProgress();
        forkRepo.setText(numberFormat.format(repoModel.getForksCount()));
        starRepo.setText(numberFormat.format(repoModel.getStargazersCount()));
        watchRepo.setText(numberFormat.format(repoModel.getSubsCount()));
        if (repoModel.getOwner() != null) {
            avatarLayout.setUrl(repoModel.getOwner().getAvatarUrl(), repoModel.getOwner().getLogin());
        } else if (repoModel.getOrganization() != null) {
            avatarLayout.setUrl(repoModel.getOrganization().getAvatarUrl(), repoModel.getOrganization().getLogin());
        }
        size.setText(ParseDateFormat.getTimeAgo(repoModel.getCreatedAt()));
        date.setText(ParseDateFormat.getTimeAgo(repoModel.getUpdatedAt()));
        title.setText(repoModel.getFullName());
        license.setVisibility(repoModel.getLicense() != null ? View.VISIBLE : View.GONE);
        if (repoModel.getLicense() != null) license.setText(repoModel.getLicense().getSpdxId());
        supportInvalidateOptionsMenu();
    }

    @Override public void onShowProgress() {
        showProgress(0);
    }

    @Override public void onShowMessage(@NonNull String msg) {
        hideProgress();
        showMessage(getString(R.string.error), msg);
    }

    @Override public void onRepoWatched(boolean isWatched) {
        watchRepo.tintDrawables(isWatched ? carrotColor : Color.BLACK);
        onEnableDisableWatch(true);
    }

    @Override public void onRepoStarred(boolean isStarred) {
        starRepo.tintDrawables(isStarred ? carrotColor : Color.BLACK);
        onEnableDisableStar(true);
    }

    @Override public void onRepoForked(boolean isForked) {
        forkRepo.tintDrawables(isForked ? carrotColor : Color.BLACK);
        onEnableDisableFork(true);
    }

    @Override public void onEnableDisableWatch(boolean isEnabled) {
        watchRepo.setEnabled(isEnabled);
    }

    @Override public void onEnableDisableStar(boolean isEnabled) {
        starRepo.setEnabled(isEnabled);
    }

    @Override public void onEnableDisableFork(boolean isEnabled) {
        forkRepo.setEnabled(isEnabled);
    }

    @Override public void onChangeWatchedCount(boolean isWatched) {
        long count = InputHelper.toLong(watchRepo);
        watchRepo.setText(numberFormat.format(isWatched ? (count + 1) : (count > 0 ? (count - 1) : 0)));
    }

    @Override public void onChangeStarCount(boolean isStarred) {
        long count = InputHelper.toLong(starRepo);
        starRepo.setText(numberFormat.format(isStarred ? (count + 1) : (count > 0 ? (count - 1) : 0)));
    }

    @Override public void onChangeForkCount(boolean isForked) {
        long count = InputHelper.toLong(forkRepo);
        forkRepo.setText(numberFormat.format(isForked ? (count + 1) : (count > 0 ? (count - 1) : 0)));
    }

    @Override public void onShowBadgeCount(@IdRes int id, int count) {
        CountBadgeProvider countBadgeProvider = (CountBadgeProvider) bottomNavigation.getBadgeProvider();
        countBadgeProvider.show(id, count);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        RepoModel repoModel = getPresenter().getRepo();
        if (repoModel != null && repoModel.isFork() && repoModel.getParent() != null) {
            MenuItem menuItem = menu.findItem(R.id.originalRepo);
            menuItem.setVisible(true);
            menuItem.setTitle(repoModel.getParent().getFullName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (getPresenter().getRepo() != null) ActivityHelper.shareUrl(this, getPresenter().getRepo().getHtmlUrl());
            return true;
        } else if (item.getItemId() == R.id.originalRepo) {
            if (getPresenter().getRepo() != null && getPresenter().getRepo().getParent() != null/*not really needed*/) {
                SchemeParser.launchUri(this, Uri.parse(getPresenter().getRepo().getParent().getFullName()));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
