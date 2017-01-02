package com.fastaccess.data.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.BuildConfig;
import com.fastaccess.data.dao.AccessTokenModel;
import com.fastaccess.data.dao.CommentRequestModel;
import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.data.dao.CommitModel;
import com.fastaccess.data.dao.CreateGistModel;
import com.fastaccess.data.dao.EventsModel;
import com.fastaccess.data.dao.GistsModel;
import com.fastaccess.data.dao.IssueEventModel;
import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.dao.IssueRequestModel;
import com.fastaccess.data.dao.Pageable;
import com.fastaccess.data.dao.PullRequestModel;
import com.fastaccess.data.dao.ReleasesModel;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.data.dao.SearchCodeModel;
import com.fastaccess.data.dao.UserModel;
import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.data.rest.service.GistService;
import com.fastaccess.data.rest.service.IssueService;
import com.fastaccess.data.rest.service.PullRequestService;
import com.fastaccess.data.rest.service.RepoService;
import com.fastaccess.data.rest.service.RestService;
import com.fastaccess.data.rest.service.SearchService;
import com.fastaccess.provider.rest.AuthRestProvider;
import com.fastaccess.provider.rest.RestProvider;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Kosh on 09 Nov 2016, 11:44 PM
 */

public class RestClient {

    public static Observable<Response<AccessTokenModel>> getAccessToken(@NonNull String code) {
        return AuthRestProvider.getLoginRestService().getAccessToken(code, BuildConfig.GITHUB_CLIENT_ID,
                BuildConfig.GITHUB_SECRET, BuildConfig.APPLICATION_ID, BuildConfig.REDIRECT_URL);
    }

    public static Observable<Response<UserModel>> getUser() {
        return RestProvider.getUserRestService().getUser();
    }

    public static Observable<Pageable<EventsModel>> getReceivedEvents(int page) {
        UserModel userModel = UserModel.getUser();
        if (userModel == null || userModel.getLogin() == null) throw new NullPointerException("User is Null");
        return RestProvider.getUserRestService().getReceivedEvents(userModel.getLogin(), page);
    }

    public static Observable<GistsModel> createGist(@NonNull CreateGistModel gistModel) {
        return RestProvider.createService(GistService.class).createGist(gistModel);
    }

    public static Observable<Response<Boolean>> deleteGist(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).deleteGist(gistId);
    }

    public static Observable<Pageable<GistsModel>> getPublicGists(int page) {
        return RestProvider.createService(GistService.class).getPublicGists(RestProvider.PAGE_SIZE, page);
    }

    public static Observable<Pageable<GistsModel>> getUserGists(@NonNull String login, int page) {
        return RestProvider.createService(GistService.class).getUserGists(login, RestProvider.PAGE_SIZE, page);
    }

    public static Observable<GistsModel> getGist(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).getGist(gistId);
    }

    public static Observable<Pageable<CommentsModel>> getGistComments(@NonNull String id, int page) {
        return RestProvider.createService(GistService.class).getGistComments(id, page);
    }

    public static Observable<Response<Boolean>> isGistStarred(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).checkGistStar(gistId);
    }

    public static Observable<Response<Boolean>> starGist(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).starGist(gistId);
    }

    public static Observable<Response<Boolean>> unStarGist(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).unStarGist(gistId);
    }

    public static Observable<Response<GistsModel>> forkGist(@NonNull String gistId) {
        return RestProvider.createService(GistService.class).forkGist(gistId);
    }

    public static Observable<CommentsModel> createGistComment(@NonNull String gistId, @NonNull CommentRequestModel comment) {
        return RestProvider.createService(GistService.class).createGistComment(gistId, comment);
    }

    public static Observable<CommentsModel> editGistComment(@NonNull String gistId, long commentId, @NonNull CommentRequestModel comment) {
        return RestProvider.createService(GistService.class).editGistComment(gistId, commentId, comment);
    }

    public static Observable<Response<Boolean>> deleteGistComment(@NonNull String gistId, long commentId) {
        return RestProvider.createService(GistService.class).deleteGistComment(gistId, commentId);
    }

    public static Observable<String> getFileData(@NonNull String baseUrl) {
        baseUrl = baseUrl.contains("/blobs/") ? baseUrl : baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";//blobs does not need / but gits yes.
        return RestProvider.createStringService(RestService.class).getFileAsStream(baseUrl);
    }

    public static Observable<Pageable<RepoModel>> getRepos(@NonNull String username, int page) {
        return RestProvider.getUserRestService().getRepos(username, page);
    }

    public static Observable<Pageable<RepoModel>> getStarred(@NonNull String username, int page) {
        return RestProvider.getUserRestService().getStarred(username, page);
    }

    public static Observable<Pageable<UserModel>> getFollowing(@NonNull String username, int page) {
        return RestProvider.getUserRestService().getFollowing(username, page);
    }

    public static Observable<Pageable<UserModel>> getFollowers(@NonNull String username, int page) {
        return RestProvider.getUserRestService().getFollowers(username, page);
    }

    public static Observable<Response<Boolean>> getFollowStatus(@NonNull String username) {
        return RestProvider.getUserRestService().getFollowStatus(username);
    }

    public static Observable<Response<Boolean>> followUnfollowUser(@NonNull String username, boolean isFollowing) {
        return !isFollowing ? RestProvider.getUserRestService().followUser(username) : RestProvider.getUserRestService().unfollowUser(username);
    }

    public static Observable<Pageable<UserModel>> searchUsers(@NonNull String query, int page) {
        return RestProvider.createService(SearchService.class).searchUsers(query, page);
    }

    public static Observable<Pageable<RepoModel>> searchRepos(@NonNull String query, int page) {
        return RestProvider.createService(SearchService.class).searchRepositories(query, page);
    }

    public static Observable<Pageable<IssueModel>> searchIssues(@NonNull String query, int page) {
        return RestProvider.createService(SearchService.class).searchIssues(query, page);
    }

    public static Observable<Pageable<SearchCodeModel>> searchCode(@NonNull String query, int page) {
        return RestProvider.createService(SearchService.class).searchCode(query, page);
    }

    public static Observable<RepoModel> getRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).getRepo(login, repoId);
    }

    public static Observable<IssueModel> getIssue(@NonNull String login, @NonNull String repoId, int number) {
        return RestProvider.createService(IssueService.class).getIssue(login, repoId, number);
    }

    public static Observable<Response<Boolean>> lockIssue(@NonNull String login, @NonNull String repoId, int number) {
        return RestProvider.createService(IssueService.class).lockIssue(login, repoId, number);
    }

    public static Observable<Response<Boolean>> unLockIssue(@NonNull String login, @NonNull String repoId, int number) {
        return RestProvider.createService(IssueService.class).unlockIssue(login, repoId, number);
    }

    public static Observable<IssueModel> editIssue(@NonNull String login, @NonNull String repoId,
                                                   int number, @NonNull IssueRequestModel model) {
        return RestProvider.createService(IssueService.class).editIssue(login, repoId, number, model);
    }

    public static Observable<Pageable<IssueModel>> getRepoIssues(@NonNull String login, @NonNull String repoId, @NonNull IssueState state, int page) {
        return RestProvider.createService(IssueService.class).getRepositoryIssues(login, repoId, state.name(), page);
    }

    public static Observable<Pageable<IssueEventModel>> getIssueTimeline(@NonNull String login, @NonNull String repoId, int number, int page) {
        return RestProvider.createService(IssueService.class).getTimeline(login, repoId, number, page);
    }

    public static Observable<Pageable<CommentsModel>> getIssuesComments(@NonNull String login, @NonNull String repoId, int number, int page) {
        return RestProvider.createService(IssueService.class).getIssueComments(login, repoId, number, page);
    }

    public static Observable<CommentsModel> createIssueComment(@NonNull String login, @NonNull String repoId,
                                                               int number, @NonNull CommentRequestModel commentsModel) {
        return RestProvider.createService(IssueService.class).createIssueComment(login, repoId, number, commentsModel);
    }

    public static Observable<CommentsModel> editIssueComment(@NonNull String login, @NonNull String repoId, long commentId,
                                                             @NonNull CommentRequestModel commentsModel) {
        return RestProvider.createService(IssueService.class).editIssueComment(login, repoId, commentId, commentsModel);
    }

    public static Observable<Response<Boolean>> deleteIssueComment(@NonNull String login, @NonNull String repoId, long commentId) {
        return RestProvider.createService(IssueService.class).deleteIssueComment(login, repoId, commentId);
    }

    public static Observable<Pageable<PullRequestModel>> getRepoPullRequests(@NonNull String login, @NonNull String repoId,
                                                                             @NonNull IssueState state, int page) {
        return RestProvider.createService(PullRequestService.class).getPullRequests(login, repoId, state.name(), page);
    }

    public static Observable<PullRequestModel> getRepoPullRequest(@NonNull String login, @NonNull String repoId, int number) {
        return RestProvider.createService(PullRequestService.class).getPullRequest(login, repoId, number);
    }

    public static Observable<String> getRepoReadme(@NonNull String login, @NonNull String repoId, @Nullable String ref) {
        return RestProvider.createStringService(RepoService.class).getReadmeHtml(login, repoId, ref);
    }

    public static Observable<Response<Boolean>> checkStarringRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).checkStarring(login, repoId);
    }

    public static Observable<Response<Boolean>> checkWatchingRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).isWatchingRepo(login, repoId);
    }

    public static Observable<Response<Boolean>> watchRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).watchRepo(login, repoId);
    }

    public static Observable<Response<Boolean>> unwatchRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).unwatchRepo(login, repoId);
    }

    public static Observable<Response<Boolean>> starRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).starRepo(login, repoId);
    }

    public static Observable<Response<Boolean>> unstarRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).unstarRepo(login, repoId);
    }

    public static Observable<RepoModel> forkRepo(@NonNull String login, @NonNull String repoId) {
        return RestProvider.createService(RepoService.class).forkRepo(login, repoId);
    }

    public static Observable<Pageable<CommitModel>> getCommits(@NonNull String login, @NonNull String repoId, int page) {
        return RestProvider.createService(RepoService.class).getCommits(login, repoId, page);
    }

    public static Observable<CommitModel> getCommit(@NonNull String login, @NonNull String repoId, @NonNull String sha) {
        return RestProvider.createService(RepoService.class).getCommit(login, repoId, sha);
    }

    public static Observable<Pageable<CommentsModel>> getCommitComments(@NonNull String login, @NonNull String repoId,
                                                                        @NonNull String sha, int page) {
        return RestProvider.createService(RepoService.class).getCommitComments(login, repoId, sha, page);
    }

    public static Observable<Pageable<ReleasesModel>> getReleases(@NonNull String login, @NonNull String repoId, int page) {
        return RestProvider.createService(RepoService.class).getReleases(login, repoId, page);
    }

    public static Observable<Pageable<UserModel>> getContributors(@NonNull String login, @NonNull String repoId, int page) {
        return RestProvider.createService(RepoService.class).getContributors(login, repoId, page);
    }

}



