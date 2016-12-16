package com.fastaccess.data.rest.service;

import android.support.annotation.NonNull;

import com.fastaccess.data.dao.CommentsModel;
import com.fastaccess.data.dao.Pageable;
import com.fastaccess.data.dao.RepoModel;
import com.fastaccess.data.dao.UserModel;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kosh on 10 Dec 2016, 3:16 PM
 */
public interface RepoService {


    @GET("repos/{login}/{repoId}")
    Observable<RepoModel> getRepo(@Path("login") String login, @Path("repoId") String repoId);

    @GET("repos/{owner}/{repo}/readme")
    @Headers("Accept: application/vnd.github.html")
    Observable<String> getReadmeHtml(@Path("owner") String owner, @Path("repo") String repo, @Query("ref") String ref);


    @GET("repos/{owner}/{repo}/issues/comments")
    Observable<Pageable<CommentsModel>> getRepoComments(@Path("owner") String owner,
                                                        @Path("repo") String repo,
                                                        @Query("page") long page);

    @GET("user/starred/{owner}/{repo}")
    Observable<Response<Boolean>> checkStarring(@NonNull @Path("owner") String login, @NonNull @Path("repo") String repoId);

    @PUT("user/starred/{owner}/{repo}")
    Observable<Response<Boolean>> starRepo(@NonNull @Path("owner") String login, @NonNull @Path("repo") String repoId);

    @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<Boolean>> unstarRepo(@NonNull @Path("owner") String login, @NonNull @Path("repo") String repoId);

    @POST("/repos/{owner}/{repo}/forks")
    Observable<RepoModel> forkRepo(@NonNull @Path("owner") String login, @NonNull @Path("repo") String repoId);

    @GET("repos/{owner}/{repo}/subscribers")
    Observable<Pageable<UserModel>> getRepoWatchers(@Path("owner") String owner, @Path("repo") String repo, @Query("page") long page);

    @GET("users/{username}/subscriptions")
    Observable<Pageable<RepoModel>> getWatchedRepos(@Path("username") String username, @Query("page") long page);

    @GET("user/subscriptions/{owner}/{repo}")
    Observable<Response<Boolean>> isWatchingRepo(@Path("owner") String owner, @Path("repo") String repo);

    @PUT("user/subscriptions/{owner}/{repo}")
    Observable<Response<Boolean>> watchRepo(@Path("owner") String owner, @Path("repo") String repo);

    @DELETE("user/subscriptions/{owner}/{repo}")
    Observable<Response<Boolean>> unwatchRepo(@Path("owner") String owner, @Path("repo") String repo);
}
