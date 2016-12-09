package com.fastaccess.data.rest.service;

import com.fastaccess.data.dao.RepoModel;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Kosh on 09 Nov 2016, 11:27 PM
 */

public interface RestService {

    @GET Observable<ResponseBody> getFileAsStream(@Url String url);

    @GET("repos/{login}/{repoId}")
    Observable<RepoModel> getRepo(@Path("login") String login, @Path("repoId") String repoId);

}
