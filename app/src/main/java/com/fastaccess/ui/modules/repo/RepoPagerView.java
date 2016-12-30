package com.fastaccess.ui.modules.repo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.FragmentPagerAdapterModel;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.adapter.FragmentsPagerAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ViewPagerView;

import java.text.NumberFormat;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 09 Dec 2016, 4:17 PM
 */

public class RepoPagerView extends BaseActivity<RepoPagerMvp.View, RepoPagerPresenter> implements RepoPagerMvp.View {


    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.size) FontTextView size;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.forkRepo) FontTextView forkRepo;
    @BindView(R.id.starRepo) FontTextView starRepo;
    @BindView(R.id.watchRepo) FontTextView watchRepo;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.pager) ViewPagerView pager;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindColor(R.color.carrot) int carrotColor;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static void startRepoPager(@NonNull Context context, @NonNull RepoModel repoModel) {
        Intent intent = new Intent(context, RepoPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.ITEM, repoModel).end());
        if (AppHelper.isOnline(context)) {
            context.startActivity(createIntent(context, repoModel.getName(), repoModel.getOwner().getLogin()));//for the sake of subs count.
        } else {
            context.startActivity(createIntent(context, repoModel.getName(), repoModel.getOwner().getLogin()));
        }
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login) {
        Intent intent = new Intent(context, RepoPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA_ID, login).put(BundleConstant.ID, repoId).end());
        return intent;
    }

    @OnClick({R.id.forkRepo, R.id.starRepo, R.id.watchRepo, R.id.share}) public void onClick(View view) {
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
            case R.id.share:
                if (getPresenter().getRepo() != null) ActivityHelper.shareUrl(this, getPresenter().getRepo().getHtmlUrl());
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
        setTitle("");
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
        } else {
            if (getPresenter().getRepo() != null) {
                onInitRepo();
            }
        }
    }

    @Override public void onFinishActivity() {
        finish();
    }

    @Override public void onInitRepo() {
        if (getPresenter().getRepo() == null) {
            finish();
            return;
        }
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
        pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapterModel
                .buildForRepo(this, getPresenter().getRepo())));
        tabs.setupWithViewPager(pager);
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
}
