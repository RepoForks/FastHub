package com.fastaccess.data.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.fastaccess.R;
import com.fastaccess.ui.modules.main.profile.followers.ProfileFollowersView;
import com.fastaccess.ui.modules.main.profile.following.ProfileFollowingView;
import com.fastaccess.ui.modules.main.profile.gists.ProfileGistsView;
import com.fastaccess.ui.modules.main.profile.overview.ProfileOverviewView;
import com.fastaccess.ui.modules.main.profile.repos.ProfileReposView;
import com.fastaccess.ui.modules.main.profile.starred.ProfileStarredView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 03 Dec 2016, 9:26 AM
 */

public class ProfilePagerAdapterModel {

    private String title;
    private Fragment fragment;

    public ProfilePagerAdapterModel(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static List<ProfilePagerAdapterModel> buildFragments(@NonNull Context context, @NonNull String login) {
        List<ProfilePagerAdapterModel> fragments = new ArrayList<>();
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.overview), ProfileOverviewView.newInstance(login)));
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.repos), ProfileReposView.newInstance(login)));
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.starred), ProfileStarredView.newInstance(login)));
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.gists), ProfileGistsView.newInstance(login)));
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.followers), ProfileFollowersView.newInstance(login)));
        fragments.add(new ProfilePagerAdapterModel(context.getString(R.string.following), ProfileFollowingView.newInstance(login)));
        return fragments;
    }
}
