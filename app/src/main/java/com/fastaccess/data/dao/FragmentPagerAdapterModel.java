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
import com.fastaccess.ui.modules.repo.issues.RepoIssuesView;
import com.fastaccess.ui.modules.search.code.SearchCodeView;
import com.fastaccess.ui.modules.search.issues.SearchIssuesView;
import com.fastaccess.ui.modules.search.repos.SearchReposView;
import com.fastaccess.ui.modules.search.users.SearchUsersView;
import com.fastaccess.ui.modules.viewer.ViewerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 03 Dec 2016, 9:26 AM
 */

public class FragmentPagerAdapterModel {

    private String title;
    private Fragment fragment;

    public FragmentPagerAdapterModel(String title, Fragment fragment) {
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

    public static List<FragmentPagerAdapterModel> buildForProfile(@NonNull Context context, @NonNull String login) {
        List<FragmentPagerAdapterModel> fragments = new ArrayList<>();
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.overview), ProfileOverviewView.newInstance(login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.repos), ProfileReposView.newInstance(login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.starred), ProfileStarredView.newInstance(login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.gists), ProfileGistsView.newInstance(login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.followers), ProfileFollowersView.newInstance(login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.following), ProfileFollowingView.newInstance(login)));
        return fragments;
    }

    public static List<FragmentPagerAdapterModel> buildForSearch(@NonNull Context context) {
        List<FragmentPagerAdapterModel> fragments = new ArrayList<>();
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.repos), SearchReposView.newInstance()));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.users), SearchUsersView.newInstance()));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.issues), SearchIssuesView.newInstance()));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.code), SearchCodeView.newInstance()));
        return fragments;
    }

    public static List<FragmentPagerAdapterModel> buildForRepo(@NonNull Context context, @NonNull RepoModel repoModel) {
        List<FragmentPagerAdapterModel> fragments = new ArrayList<>();
        String login = repoModel.getOwner().getLogin();
        String repoId = String.valueOf(repoModel.getName());
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.readme), ViewerView.newInstance(repoId, login)));
        fragments.add(new FragmentPagerAdapterModel(context.getString(R.string.issues), RepoIssuesView.newInstance(repoId, login)));
        return fragments;
    }
}
