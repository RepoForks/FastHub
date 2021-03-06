package com.fastaccess.ui.modules.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.fastaccess.R;
import com.fastaccess.data.dao.FragmentPagerAdapterModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.adapter.FragmentsPagerAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.ViewPagerView;

import butterknife.BindView;
import icepick.State;

/**
 * Created by Kosh on 03 Dec 2016, 8:00 AM
 */

public class UserPagerView extends BaseActivity<UserPagerMvp.View, UserPagerPresenter> implements UserPagerMvp.View {


    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;
    @State String login;

    public static void startActivity(@NonNull Context context, @NonNull String login) {
        Intent intent = new Intent(context, UserPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, login).end());
        context.startActivity(intent);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String login) {
        Intent intent = new Intent(context, UserPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, login).end());
        return intent;
    }

    @Override protected int layout() {
        return R.layout.tabbed_pager_layout;
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

    @NonNull @Override public UserPagerPresenter providePresenter() {
        return new UserPagerPresenter();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            login = getIntent().getExtras().getString(BundleConstant.EXTRA);
        }
        if (InputHelper.isEmpty(login)) {
            finish();
            return;
        }
        setTitle(login);
        onInvalidateMenuItem();
        FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapterModel.buildForProfile(this, login));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        if (savedInstanceState == null) {
            getPresenter().onCheckFollowStatus(login);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.follow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.follow) {
            if (item.getActionView() == null) {
                MenuItemCompat.setActionView(item, new ProgressBar(this));
                getPresenter().onFollowMenuItemClicked(login);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        if (getPresenter().isSuccessResponse()) {
            MenuItemCompat.setActionView(menu.findItem(R.id.follow), null);
            menu.findItem(R.id.follow)
                    .setVisible(true)
                    .setTitle(getPresenter().isFollowing() ? getString(R.string.unfollow) : getString(R.string.follow));

        } else {
            MenuItemCompat.setActionView(menu.findItem(R.id.follow), new ProgressBar(this));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override public void onInvalidateMenuItem() {
        supportInvalidateOptionsMenu();
    }
}
