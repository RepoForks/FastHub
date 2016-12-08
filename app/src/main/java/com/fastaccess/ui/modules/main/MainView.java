package com.fastaccess.ui.modules.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fastaccess.R;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.main.gists.create.CreateGistView;
import com.fastaccess.ui.modules.main.home.HomeView;
import com.fastaccess.ui.modules.search.SearchView;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import it.sephiroth.android.library.bottomnavigation.BadgeProvider;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainView extends BaseActivity<MainMvp.View, MainPresenter> implements MainMvp.View {

    @MainMvp.NavigationType @State int navType;

    @BindView(R.id.container) FrameLayout container;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.bottomNavigation) BottomNavigation bottomNavigation;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.navigation) NavigationView navigation;
    private long backPressTimer;

    private BadgeProvider badgeProvider;

    @OnClick(R.id.fab) void onClick() {
        if (navType == MainMvp.GISTS) {
            startActivity(new Intent(this, CreateGistView.class));
        }
    }

    @NonNull @Override public MainPresenter providePresenter() {
        return new MainPresenter();
    }

    @Override protected int layout() {
        return R.layout.activity_main_view;
    }

    @Override protected boolean hasSlideExitAnimation() {
        return false;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override protected boolean isSecured() {
        return false;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LightTheme);
        super.onCreate(savedInstanceState);
        Logger.e(savedInstanceState);
        getPresenter().onActivityStarted(savedInstanceState, this, bottomNavigation, navigation);
        Logger.e(PrefGetter.getToken());
        if (null != savedInstanceState) {
            getBadgeProvider().restore(savedInstanceState);
        } else {
            if (isLoggedIn()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, HomeView.newInstance(), HomeView.TAG)
                        .commit();
            }
        }
        onHideShowFab();

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onOpenDrawer();
            return true;
        } else if (item.getItemId() == R.id.search) {
            startActivity(new Intent(this, SearchView.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        if (drawerLayout != null) {
            if (getPresenter().canBackPress(drawerLayout)) {
                if (canExit()) super.onBackPressed();
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        } else {
            if (canExit()) super.onBackPressed();
        }
    }

    @Override public void onNavigationChanged(@MainMvp.NavigationType int navType) {
        //noinspection WrongConstant
        if (bottomNavigation.getSelectedIndex() != navType) bottomNavigation.setSelectedIndex(navType, true);
        this.navType = navType;
        getPresenter().onModuleChanged(getSupportFragmentManager(), navType);
        onHideShowFab();
    }

    @Override public void onOpenDrawer() {
        if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override public void onCloseDrawer() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override public void onOpenSettings() {

    }

    @Override public void onShowBadge(@IdRes int itemId) {
        if (!getBadgeProvider().hasBadge(itemId)) {
            getBadgeProvider().show(itemId);
        }
    }

    @Override public void onHideBadge(@IdRes int itemId) {
        if (getBadgeProvider().hasBadge(itemId)) {
            getBadgeProvider().remove(itemId);
        }
    }

    @Override public void onOpenRate() {

    }

    @Override public void onHideToolbarShadow() {
        hideShadow();
    }

    @Override public void onHideShowFab() {
        if (navType == MainMvp.GISTS) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @NonNull private BadgeProvider getBadgeProvider() {
        if (badgeProvider == null) {
            badgeProvider = bottomNavigation.getBadgeProvider();
        }
        return badgeProvider;
    }

    private boolean canExit() {
        if (backPressTimer + 2000 > System.currentTimeMillis()) {
            return true;
        } else {
            Toast.makeText(getBaseContext(), R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
        }
        backPressTimer = System.currentTimeMillis();
        return false;
    }
}
