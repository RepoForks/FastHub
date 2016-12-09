package com.fastaccess.provider.intent;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.ActorModel;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.provider.rest.RestProvider;
import com.fastaccess.ui.modules.user.UserPagerView;

public class IntentsManager {

    private static final int URI_BASE = 0;
    private static final int URI_USER = 1;
    private static final int URI_REPO = 2;
    private static final int URI_REPO_NUMS = 16;
    private static final int URI_REPO_NUMS2 = 17;
    private static final int URI_REPO_NUMS3 = 18;
    private static final int URI_ISSUE = 3;
    private static final int URI_COMMIT = 4;
    private static final int URI_REPO_BRANCH = 5;
    private static final int URI_REPO_BRANCH_FEATURE = 6;
    private static final int URI_REPO_BRANCH_RELEASE = 7;
    private static final int URI_REPO_BRANCH_HOTFIX = 8;
    private static final int URI_RELEASES_TAG = 9;
    private static final int URI_RELEASES = 10;
    private static final int URI_RELEASES_IDENTIFIER = 11;
    private static final int URI_RELEASES_LATEST = 12;
    private static final int URI_TAGS = 13;
    private static final int URI_PULL_REQUEST = 14;
    private static final int URI_ISSUE_COMMENT = 15;

    private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final GitskariosUriManager gitsakriosUriManager = new GitskariosUriManager();
    private Context context;

    public IntentsManager(Context context) {
        this.context = context;
        int i = 1000;

        String host = RestProvider.REST_URL;

        uriMatcher.addURI(host, "", URI_BASE + i);
        uriMatcher.addURI(host, "*/*/releases", URI_RELEASES);
        uriMatcher.addURI(host, "*/*/releases/#", URI_RELEASES_IDENTIFIER);
        uriMatcher.addURI(host, "*/*/tags", URI_TAGS);
        uriMatcher.addURI(host, "*/*/commit/*", URI_COMMIT);
        uriMatcher.addURI(host, "*/*/issues/comments/#", URI_ISSUE_COMMENT);
        uriMatcher.addURI(host, "*/*/issues/#", URI_ISSUE);
        uriMatcher.addURI(host, "*/*/pull/#", URI_PULL_REQUEST);
        uriMatcher.addURI(host, "*/*/tree/feature/*", URI_REPO_BRANCH_FEATURE);
        uriMatcher.addURI(host, "*/*/tree/release/*", URI_REPO_BRANCH_RELEASE);
        uriMatcher.addURI(host, "*/*/tree/hotfix/*", URI_REPO_BRANCH_HOTFIX);
        uriMatcher.addURI(host, "*/*/tree/*", URI_REPO_BRANCH);
        uriMatcher.addURI(host, "*/*", URI_REPO);
        uriMatcher.addURI(host, "*/#", URI_REPO_NUMS);
        uriMatcher.addURI(host, "#/*", URI_REPO_NUMS2);
        uriMatcher.addURI(host, "#/#", URI_REPO_NUMS3);
        uriMatcher.addURI(host, "*", URI_USER);
    }

    @Nullable
    public Intent checkUri(Uri uri) {

        uri = normalizeUri(uri);

        boolean matched = uriMatcher.match(uri) > -1 && uriMatcher.match(uri) < 1000;
        Intent intent = null;
        if (matched) {
            switch (uriMatcher.match(uri)) {
                case URI_REPO:
                case URI_REPO_NUMS:
                case URI_REPO_NUMS2:
                case URI_REPO_NUMS3:
                case URI_REPO_BRANCH:
                case URI_REPO_BRANCH_FEATURE:
                case URI_REPO_BRANCH_RELEASE:
                case URI_REPO_BRANCH_HOTFIX:
                    intent = manageRepos(uri);
                    break;
                case URI_USER:
                    intent = manageUsers(uri);
                    break;
                case URI_COMMIT:
                    intent = manageCommit(uri);
                    break;
                case URI_ISSUE:
                    intent = manageIssue(uri);
                    break;
                case URI_PULL_REQUEST:
                    intent = manageIssuePullRequest(uri);
                    break;
                case URI_RELEASES_IDENTIFIER:
                    intent = manageReleasesWithId(uri);
                    break;
                case URI_RELEASES:
                case URI_RELEASES_LATEST:
                case URI_RELEASES_TAG:
                case URI_TAGS:
                case URI_ISSUE_COMMENT:
                    break;
            }
        } else if (uri.toString().contains("blob")) {
            intent = manageFile(uri);
        }
        return intent;
    }

    private Uri normalizeUri(Uri uri) {
        if (uri != null && uri.getAuthority() != null) {
            if (uri.getPath().contains("/api/v3/")) {
                String authority = uri.getPath().replace("/api/v3/", "/");
                uri = uri.buildUpon().path(authority).build();
            } else if (uri.getAuthority().contains("api.")) {
                String authority = uri.getAuthority().replace("api.", "");
                uri = uri.buildUpon().authority(authority).build();
            }

            if (uri.getPath().contains("repos/")) {
                String path = uri.getPath().replace("repos/", "");
                uri = uri.buildUpon().path(path).build();
            }
            if (uri.getPath().contains("commits/")) {
                String path = uri.getPath().replace("commits/", "commit/");
                uri = uri.buildUpon().path(path).build();
            }
            if (uri.getPath().contains("pulls/")) {
                String path = uri.getPath().replace("pulls/", "pull/");
                uri = uri.buildUpon().path(path).build();
            }
        }
        return uri;
    }

    public Intent manageRepos(Uri uri) {
        uri = normalizeUri(uri);
        RepoModel repoInfo = gitsakriosUriManager.getRepoInfo(uri);
//        return RepoDetailActivity.createLauncherIntent(context, repoInfo);
        return null;
    }

    public Intent manageUsers(Uri uri) {
        ActorModel user = gitsakriosUriManager.getUser(uri);
        Intent intent = new Intent(context, UserPagerView.class);
        intent.putExtras(Bundler.start().put(BundleConstant.EXTRA, user.getLogin()).end());
        return intent;
    }

    private Intent manageCommit(Uri uri) {
//        CommitInfo info = gitsakriosUriManager.getCommitInfo(uri);
//
//        return CommitDetailActivity.launchIntent(context, info);
        return null;
    }

    private Intent manageIssue(Uri uri) {
//        IssueInfo info = gitsakriosUriManager.getIssueCommentInfo(uri);
//
//        return IssueDetailActivity.createLauncherIntent(context, info);
        return null;
    }

    private Intent manageIssuePullRequest(Uri uri) {
//        IssueInfo info = gitsakriosUriManager.getIssueInfo(uri);
//
//        return PullRequestDetailActivity.createLauncherIntent(context, info);
        return null;
    }

    private Intent manageReleasesWithId(Uri uri) {
//        ReleaseInfo info = gitsakriosUriManager.getReleaseInfo(uri);
//
//        return ReleaseDetailActivity.launchIntent(context, info);
        return null;
    }

    private Intent manageFile(Uri uri) {
//        FilesListModel info = gitsakriosUriManager.getFileInfo(uri);
//        return FileActivity.createLauncherIntent(context, info);
        return null;
    }
}
