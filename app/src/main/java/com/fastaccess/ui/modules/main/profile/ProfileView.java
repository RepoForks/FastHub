package com.fastaccess.ui.modules.main.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.ProfilePagerAdapterModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.ui.adapter.ProfilePagerAdapter;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.ViewPagerView;

import butterknife.BindView;

/**
 * Created by Kosh on 03 Dec 2016, 8:00 AM
 */

public class ProfileView extends BaseFragment<ProfileMvp.View, ProfilePresenter> implements ProfileMvp.View {

    public static final String TAG = ProfileView.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPagerView pager;

    public static ProfileView newInstance(@NonNull String login) {
        ProfileView profileView = new ProfileView();
        profileView.setArguments(Bundler.start().put(BundleConstant.EXTRA, login).end());
        return profileView;
    }

    @Override protected int fragmentLayout() {
        return R.layout.tabbed_pager_layout;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            throw new RuntimeException("Bundle is null?");
        }
        navigationCallback.onSetupToolbar(toolbar, R.drawable.ic_menu);
        String login = getArguments().getString(BundleConstant.EXTRA);
        if (login == null) {
            throw new RuntimeException("user is null?");
        }
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getChildFragmentManager(),
                ProfilePagerAdapterModel.buildFragments(getContext(), login));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        Logger.e(pager.getOffscreenPageLimit());
    }

    @NonNull @Override public ProfilePresenter providePresenter() {
        return new ProfilePresenter();
    }
}
