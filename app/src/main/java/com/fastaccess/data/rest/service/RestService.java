package com.fastaccess.data.rest.service;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Kosh on 09 Nov 2016, 11:27 PM
 */

public interface RestService {

    @GET @Headers("Accept: application/vnd.github.VERSION.raw") Observable<String> getFileAsStream(@Url String url);
}
