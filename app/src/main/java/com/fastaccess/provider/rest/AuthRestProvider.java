package com.fastaccess.provider.rest;

import android.support.annotation.NonNull;

import com.fastaccess.data.rest.service.UserRestService;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.provider.rest.implementation.PaginationInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kosh on 14 Dec 2016, 11:22 PM
 */

public class AuthRestProvider {

    @NonNull public static UserRestService getLoginRestService() {
        return new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl("https://github.com/login/oauth/")
                .addConverterFactory(GsonConverterFactory.create(RestProvider.getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(UserRestService.class);
    }

    @NonNull private static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new PaginationInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/json");
                    if (!InputHelper.isEmpty(PrefGetter.getToken())) {
                        requestBuilder.header("Authorization", "token " + PrefGetter.getToken());
                    }
                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }
}

