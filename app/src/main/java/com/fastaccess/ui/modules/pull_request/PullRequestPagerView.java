package com.fastaccess.ui.modules.pull_request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.FragmentPagerAdapterModel;
import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.data.dao.PullsIssuesParser;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.adapter.FragmentsPagerAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.pull_request.comments.PullRequestCommentsView;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.ViewPagerView;
import com.fastaccess.ui.widgets.dialog.MessageDialogView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 10 Dec 2016, 9:23 AM
 */

public class PullRequestPagerView extends BaseActivity<PullRequestPagerMvp.View, PullRequestPagerPresenter> implements PullRequestPagerMvp.View {

    @BindView(R.id.startGist) ForegroundImageView startGist;
    @BindView(R.id.forkGist) ForegroundImageView forkGist;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.headerTitle) FontTextView title;
    @BindView(R.id.size) FontTextView size;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;
    @BindView(R.id.fab) FloatingActionButton fab;

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login, int number) {
        Intent intent = new Intent(context, PullRequestPagerView.class);
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ID, number)
                .put(BundleConstant.EXTRA_ID, login)
                .put(BundleConstant.EXTRA2_ID, repoId)
                .end());
        return intent;

    }

    public static void createIntentForOffline(@NonNull Context context, @NonNull PullRequestModel pullRequest) {
        PullsIssuesParser pullsIssuesParser = PullsIssuesParser.getForPullRequest(pullRequest.getHtmlUrl());
        if (pullsIssuesParser == null) throw new NullPointerException("Pulls Parser is null");
        Logger.e(pullsIssuesParser);
        pullRequest.setLogin(pullsIssuesParser.getLogin());
        pullRequest.setRepoId(pullsIssuesParser.getRepoId());
        pullRequest.setNumber(pullsIssuesParser.getNumber());
        Intent intent = new Intent(context, PullRequestPagerView.class);
        intent.putExtras(Bundler.start()
                .put(BundleConstant.ITEM, pullRequest)
                .end());
        context.startActivity(intent);

    }

    @OnClick(R.id.fab) void onAddComment() {
        PullRequestCommentsView view = (PullRequestCommentsView) pager.getAdapter().instantiateItem(pager, 1);
        if (view != null) {
            view.onStartNewComment();
        }
    }

    @Override protected int layout() {
        return R.layout.issue_pager_activity;
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

    @NonNull @Override public PullRequestPagerPresenter providePresenter() {
        return new PullRequestPagerPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityCreated(getIntent());
        } else {
            onSetupIssue();
        }
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        startGist.setVisibility(View.GONE);
        forkGist.setVisibility(View.GONE);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.issue_menu, menu);
        menu.findItem(R.id.closeIssue).setVisible(getPresenter().isOwner());
        menu.findItem(R.id.lockIssue).setVisible(getPresenter().isOwner());
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (getPresenter().getPullRequest() != null) ActivityHelper.shareUrl(this, getPresenter().getPullRequest().getHtmlUrl());
            return true;
        } else if (item.getItemId() == R.id.closeIssue) {
            PullRequestModel pullRequest = getPresenter().getPullRequest();
            if (pullRequest == null) return true;
            MessageDialogView.newInstance(
                    pullRequest.getState() == IssueState.open ? getString(R.string.close_issue) : getString(R.string.re_open_issue),
                    getString(R.string.confirm_message), Bundler.start().put(BundleConstant.EXTRA, true).end())
                    .show(getSupportFragmentManager(), MessageDialogView.TAG);
            return true;
        } else if (item.getItemId() == R.id.lockIssue) {
            MessageDialogView.newInstance(
                    getPresenter().isLocked() ? getString(R.string.unlock_issue) : getString(R.string.lock_issue),
                    getPresenter().isLocked() ? getString(R.string.unlock_issue_details) : getString(R.string.lock_issue_details),
                    Bundler.start().put(BundleConstant.EXTRA_ID, true).end())
                    .show(getSupportFragmentManager(), MessageDialogView.TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem closeIssue = menu.findItem(R.id.closeIssue);
        MenuItem lockIssue = menu.findItem(R.id.lockIssue);
        boolean isOwner = getPresenter().isOwner();
        boolean isLocked = getPresenter().isLocked();
        menu.findItem(R.id.closeIssue).setVisible(isOwner);
        menu.findItem(R.id.lockIssue).setVisible(isOwner);
        if (isOwner) {
            //noinspection ConstantConditions ( getIssue at this stage is not null but AS doesn't know. )
            closeIssue.setTitle(getPresenter().getPullRequest().getState() == IssueState.closed ? getString(R.string.re_open) : getString(R.string
                    .close));
            lockIssue.setTitle(isLocked ? getString(R.string.unlock_issue) : getString(R.string.lock_issue));
        }

        return super.onPrepareOptionsMenu(menu);
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

    @Override public void onSetupIssue() {
        hideProgress();
        if (getPresenter().getPullRequest() == null) {
            finish();
            return;
        }
        supportInvalidateOptionsMenu();
        PullRequestModel pullRequest = getPresenter().getPullRequest();
        setTitle(String.format("#%s", pullRequest.getNumber()));
        UserModel userModel = pullRequest.getUser();
        title.setText(pullRequest.getTitle());
        if (userModel != null) {
            date.setVisibility(View.GONE);
            size.setText(SpannableBuilder.builder().append(getString(pullRequest.getState().getStatus()))
                    .append(" ").append(getString(R.string.by)).append(" ").append(userModel.getLogin()).append(" ")
                    .append(ParseDateFormat.getTimeAgo(pullRequest.getCreatedAt())));
            avatarLayout.setUrl(userModel.getAvatarUrl(), userModel.getLogin());
        }
        pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapterModel.buildForPullRequest(this, pullRequest)));
        tabs.setupWithViewPager(pager);
        if (!getPresenter().isLocked() && !getPresenter().isOwner()) {
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    hideShowFab();
                }
            });
        }
        hideShowFab();
    }

    @Override public void showSuccessIssueActionMsg(boolean isClose) {
        hideProgress();
        if (isClose) {
            showMessage(getString(R.string.success), getString(R.string.success_closed));
        } else {
            showMessage(getString(R.string.success), getString(R.string.success_re_opened));
        }
    }

    @Override public void showErrorIssueActionMsg(boolean isClose) {
        hideProgress();
        if (isClose) {
            showMessage(getString(R.string.error), getString(R.string.error_closing_issue));
        } else {
            showMessage(getString(R.string.error), getString(R.string.error_re_opening_issue));
        }
    }

    @Override public void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle) {
        super.onMessageDialogActionClicked(isOk, bundle);
        if (isOk) {
            getPresenter().onHandleConfirmDialog(bundle);
        }
    }

    private void hideShowFab() {
        if (getPresenter().isLocked() && !getPresenter().isOwner()) {
            fab.hide();
            return;
        }
        if (pager.getCurrentItem() == 1) {
            fab.show();
        } else {
            fab.hide();
        }
    }
}
