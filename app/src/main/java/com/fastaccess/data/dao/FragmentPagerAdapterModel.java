package com.fastaccess.data.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fastaccess.R;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.ui.modules.issue.comments.IssueCommentsView;
import com.fastaccess.ui.modules.issue.details.IssueDetailsView;
import com.fastaccess.ui.modules.main.profile.followers.ProfileFollowersView;
import com.fastaccess.ui.modules.main.profile.following.ProfileFollowingView;
import com.fastaccess.ui.modules.main.profile.gists.ProfileGistsView;
import com.fastaccess.ui.modules.main.profile.overview.ProfileOverviewView;
import com.fastaccess.ui.modules.main.profile.repos.ProfileReposView;
import com.fastaccess.ui.modules.main.profile.starred.ProfileStarredView;
import com.fastaccess.ui.modules.pull_request.details.PullRequestDetailsView;
import com.fastaccess.ui.modules.repo.issues.lists.RepoIssuesView;
import com.fastaccess.ui.modules.repo.pull_request.lists.RepoPullRequestView;
import com.fastaccess.ui.modules.search.code.SearchCodeView;
import com.fastaccess.ui.modules.search.issues.SearchIssuesView;
import com.fastaccess.ui.modules.search.repos.SearchReposView;
import com.fastaccess.ui.modules.search.users.SearchUsersView;
import com.fastaccess.ui.modules.viewer.ViewerView;

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

    @NonNull public static List<FragmentPagerAdapterModel> buildForProfile(@NonNull Context context, @NonNull String login) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.overview), ProfileOverviewView.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.repos), ProfileReposView.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.starred), ProfileStarredView.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.gists), ProfileGistsView.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.followers), ProfileFollowersView.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.following), ProfileFollowingView.newInstance(login)))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForSearch(@NonNull Context context) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.repos), SearchReposView.newInstance()),
                new FragmentPagerAdapterModel(context.getString(R.string.users), SearchUsersView.newInstance()),
                new FragmentPagerAdapterModel(context.getString(R.string.issues), SearchIssuesView.newInstance()),
                new FragmentPagerAdapterModel(context.getString(R.string.code), SearchCodeView.newInstance()))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForIssues(@NonNull Context context, @NonNull IssueModel issueModel) {
        String login = issueModel.getLogin();
        String repoId = issueModel.getRepoId();
        int number = issueModel.getNumber();
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.details), IssueDetailsView.newInstance(issueModel)),
                new FragmentPagerAdapterModel(context.getString(R.string.comments), IssueCommentsView.newInstance(login, repoId, number)))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForPullRequest(@NonNull Context context, @NonNull PullRequestModel pullRequest) {
        String login = pullRequest.getLogin();
        String repoId = pullRequest.getRepoId();
        int number = pullRequest.getNumber();
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.details), PullRequestDetailsView.newInstance(pullRequest)),
                new FragmentPagerAdapterModel(context.getString(R.string.conversation), IssueCommentsView.newInstance(login, repoId, number)))
                .filter(value -> value.getTitle() != null && value.getFragment() != null)
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForRepoCode(@NonNull Context context, @NonNull RepoModel repoModel) {
        String login = repoModel.getOwner().getLogin();
        String repoId = repoModel.getName();
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.readme), ViewerView
                .newInstance(repoId, login, repoModel.getHtmlUrl())))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForRepoIssue(@NonNull Context context, @NonNull String login,
                                                                             @NonNull String repoId) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.opened),
                        RepoIssuesView.newInstance(repoId, login, IssueState.open)),
                new FragmentPagerAdapterModel(context.getString(R.string.closed),
                        RepoIssuesView.newInstance(repoId, login, IssueState.closed)))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForRepoPullRequest(@NonNull Context context, @NonNull String login,
                                                                                   @NonNull String repoId) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.opened),
                        RepoPullRequestView.newInstance(repoId, login, IssueState.open)),
                new FragmentPagerAdapterModel(context.getString(R.string.closed),
                        RepoPullRequestView.newInstance(repoId, login, IssueState.closed)))
                .collect(Collectors.toList());
    }
}
