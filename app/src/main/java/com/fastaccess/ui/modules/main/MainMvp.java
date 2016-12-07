package com.fastaccess.ui.modules.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

import com.fastaccess.ui.base.mvp.BaseMvp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by Kosh on 09 Nov 2016, 7:51 PM
 */

public interface MainMvp {

    int FEEDS = 0;
    int GISTS = 1;
    int PROFILE = 2;

    @IntDef({
            FEEDS,
            GISTS,
            PROFILE,
    })
    @Retention(RetentionPolicy.SOURCE) @interface NavigationType {}

    interface View extends BaseMvp.FAView {

        void onNavigationChanged(@NavigationType int navType);

        void onOpenDrawer();

        void onCloseDrawer();

        void onOpenSettings();

        void onShowBadge(@IdRes int itemId);

        void onHideBadge(@IdRes int itemId);

        void onOpenRate();

        void onHideToolbarShadow();

        void onHideShowFab();
    }

    interface Presenter extends BaseMvp.FAPresenter<MainMvp.View>, NavigationView.OnNavigationItemSelectedListener,
            BottomNavigation.OnMenuItemSelectionListener {

        void onActivityStarted(@Nullable Bundle savedInstance, @NonNull MainView mainView,
                               @NonNull BottomNavigation bottomNavigation,
                               @NonNull NavigationView navigationView);

        boolean canBackPress(@NonNull DrawerLayout drawerLayout);

        void onModuleChanged(@NonNull FragmentManager fragmentManager, @NavigationType int type);

        void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide);

        void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide);
    }
}
