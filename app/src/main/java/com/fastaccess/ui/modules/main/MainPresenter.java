package com.fastaccess.ui.modules.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fastaccess.R;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.TypeFaceHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.main.gists.GistsView;
import com.fastaccess.ui.modules.main.home.HomeView;
import com.fastaccess.ui.modules.main.profile.ProfileView;
import com.fastaccess.ui.widgets.AvatarLayout;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import static com.fastaccess.helper.AppHelper.getFragmentByTag;
import static com.fastaccess.helper.AppHelper.getVisibleFragment;

/**
 * Created by Kosh on 09 Nov 2016, 7:53 PM
 */

public class MainPresenter extends BasePresenter<MainMvp.View> implements MainMvp.Presenter {
    @Override public void onActivityStarted(@Nullable Bundle savedInstance,
                                            @NonNull MainView mainView,
                                            @NonNull BottomNavigation bottomNavigation,
                                            @NonNull NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
        Typeface myTypeface = TypeFaceHelper.getTypeface();
        bottomNavigation.setDefaultTypeface(myTypeface);
        bottomNavigation.setOnMenuItemClickListener(this);
        if (savedInstance == null) {
            bottomNavigation.setDefaultSelectedIndex(0);
        }
        UserModel userModel = UserModel.getUser();
        if (userModel != null) {
            View view = navigationView.getHeaderView(0);
            if (view != null) {
                ((AvatarLayout) view.findViewById(R.id.avatarLayout)).setUrl(userModel.getAvatarUrl(), userModel.getLogin());
                ((TextView) view.findViewById(R.id.username)).setText(userModel.getName());
                ((TextView) view.findViewById(R.id.email)).setText(userModel.getLogin());
            }
        }
    }

    @Override public boolean canBackPress(@NonNull DrawerLayout drawerLayout) {
        return !drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onModuleChanged(@NonNull FragmentManager fragmentManager, @MainMvp.NavigationType int type) {
        Fragment currentVisible = getVisibleFragment(fragmentManager);
        HomeView homeView = (HomeView) getFragmentByTag(fragmentManager, HomeView.TAG);
        GistsView gistsView = (GistsView) getFragmentByTag(fragmentManager, GistsView.TAG);
        ProfileView profileView = (ProfileView) getFragmentByTag(fragmentManager, ProfileView.TAG);
        switch (type) {
            case MainMvp.FEEDS:
                if (homeView == null) {
                    onAddAndHide(fragmentManager, HomeView.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, homeView, currentVisible);
                }
                break;
            case MainMvp.GISTS:
                if (gistsView == null) {
                    onAddAndHide(fragmentManager, GistsView.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, gistsView, currentVisible);
                }
                break;
            case MainMvp.PROFILE:
                if (profileView == null) {
                    onAddAndHide(fragmentManager, ProfileView.newInstance(UserModel.getUser().getLogin()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, profileView, currentVisible);
                }
                break;
        }
    }

    @Override public void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide) {
        Logger.e("show", toShow.getClass().getSimpleName(), "hide", toHide.getClass().getSimpleName());
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
        toShow.onHiddenChanged(false);
    }

    @Override public void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide) {
        Logger.e("add", toAdd.getClass().getSimpleName(), "hide", toHide.getClass().getSimpleName());
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .add(R.id.container, toAdd, toAdd.getClass().getSimpleName())
                .commit();
        toAdd.onHiddenChanged(false);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (getView() != null && isAttached()) getView().onCloseDrawer();
        return false;
    }

    @Override public void onMenuItemSelect(@IdRes int id, int position) {
        if (getView() != null && isAttached()) {
            getView().onNavigationChanged(position);
            getView().onHideBadge(id);
        }
    }

    @Override public void onMenuItemReselect(@IdRes int id, int position) {}
}
