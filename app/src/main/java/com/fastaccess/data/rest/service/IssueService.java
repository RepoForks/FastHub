package com.fastaccess.data.rest.service;


import com.fastaccess.data.dao.IssueModel;
import com.fastaccess.data.dao.IssueRequestModel;
import com.fastaccess.data.dao.Pageable;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface IssueService {

    @GET("issues")
    Observable<Pageable<IssueModel>> getIssues(@QueryMap Map<String, Object> filter, @Query("page") long page);

    @GET("user/issues")
    Observable<Pageable<IssueModel>> getRepoMemberIssues(@QueryMap Map<String, Object> filter, @Query("page") long page);

    @GET("orgs/{org}/issues")
    Observable<Pageable<IssueModel>> getOrgIssues(@QueryMap Map<String, Object> filter, @Query("page") long page);

    @GET("repos/{owner}/{repo}/issues")
    Observable<Pageable<IssueModel>> getRepositoryIssues(@Path("owner") String owner, @Path("repo") String repo,
                                                         @QueryMap Map<String, Object> filter,
                                                         @Query("page") long page);

    @GET("repos/{owner}/{repo}/issues/{number}")
    Observable<IssueModel> getIssue(@Path("owner") String owner, @Path("repo") String repo,
                                    @Path("number") long number);

    @POST("repos/{owner}/{repo}/issues")
    Observable<IssueModel> createIssue(@Path("owner") String owner, @Path("repo") String repo,
                                       @Body IssueRequestModel issue);

    @PATCH("repos/{owner}/{repo}/issues/{number}")
    Observable<IssueModel> editIssue(@Path("owner") String owner, @Path("repo") String repo,
                                     @Path("number") long number,
                                     @Body IssueRequestModel issue);

    @Headers("Content-Length: 0")
    @PUT("repos/{owner}/{repo}/issues/{number}/lock")
    Observable<Response<Boolean>> lockIssue(@Path("owner") String owner, @Path("repo") String repo, @Path("number") long number);

    @DELETE("repos/{owner}/{repo}/issues/{number}/lock")
    Observable<Response<Boolean>> unlockIssue(@Path("owner") String owner, @Path("repo") String repo, @Path("number") long number);
}