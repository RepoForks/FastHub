package com.fastaccess.ui.modules.main.profile.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.SpannableBuilder;

import butterknife.BindView;
import icepick.State;

/**
 * Created by Kosh on 03 Dec 2016, 9:16 AM
 */

public class ProfileOverviewView extends BaseFragment<ProfileOverviewMvp.View, ProfileOverviewPresenter> implements ProfileOverviewMvp.View {

    @BindView(R.id.username) FontTextView username;
    @BindView(R.id.description) FontTextView description;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.organization) FontTextView organization;
    @BindView(R.id.location) FontTextView location;
    @BindView(R.id.email) FontTextView email;
    @BindView(R.id.link) FontTextView link;
    @BindView(R.id.joined) FontTextView joined;
    @BindView(R.id.following) FontTextView following;
    @BindView(R.id.followers) FontTextView followers;
    @State UserModel userModel;
    @BindView(R.id.progress) View progress;

    public static ProfileOverviewView newInstance(String login) {
        ProfileOverviewView view = new ProfileOverviewView();
        view.setArguments(Bundler.start().put(BundleConstant.EXTRA, login).end());
        return view;
    }

    @Override protected int fragmentLayout() {
        return R.layout.profile_overview_layout;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getPresenter().onFragmentCreated(getArguments());
        } else {
            if (userModel != null) onInitViews(userModel);
            else getPresenter().onFragmentCreated(getArguments());
        }
    }

    @NonNull @Override public ProfileOverviewPresenter providePresenter() {
        return new ProfileOverviewPresenter();
    }

    @Override public void onInitViews(@Nullable UserModel userModel) {
        progress.setVisibility(View.GONE);
        if (userModel == null) return;
        this.userModel = userModel;
        username.setText(userModel.getLogin());
        description.setText(userModel.getBio());
        avatarLayout.setUrl(userModel.getAvatarUrl(), null);
        organization.setText(InputHelper.toNA(userModel.getCompany()));
        location.setText(InputHelper.toNA(userModel.getLocation()));
        email.setText(InputHelper.toNA(userModel.getEmail()));
        link.setText(InputHelper.toNA(userModel.getBlog()));
        joined.setText(userModel.getCreatedAt() != null ? ParseDateFormat.getTimeAgo(userModel.getCreatedAt()) : "N/A");
        followers.setText(SpannableBuilder.builder()
                .append(getString(R.string.followers))
                .append("\n")
                .bold(String.valueOf(userModel.getFollowers())));
        following.setText(SpannableBuilder.builder()
                .append(getString(R.string.following))
                .append("\n")
                .bold(String.valueOf(userModel.getFollowing())));
    }

    @Override public void onShowProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override public void onShowMessage(String message) {
        progress.setVisibility(View.GONE);
        if (navigationCallback != null) navigationCallback.showMessage(getString(R.string.error), message);
    }
}
