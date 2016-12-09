package com.fastaccess.provider.rest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.fastaccess.R;
import com.fastaccess.data.rest.service.RestService;
import com.fastaccess.data.rest.service.UserRestService;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.provider.rest.implementation.PaginationInterceptor;
import com.fastaccess.provider.rest.implementation.StringConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kosh on 04 Nov 2016, 2:11 PM
 */

public class RestProvider {

    public static final int PAGE_SIZE = 30;
    public static final String REST_URL = "https://api.github.com/";
    private static Gson gson;

    @NonNull private static HttpLoggingInterceptor loggingInterceptor(@NonNull HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);
        return interceptor;
    }

    @NonNull private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                .client(getHttpClient())
                .addConverterFactory(new StringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofit(@NonNull Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull public static UserRestService getLoginRestService() {
        return new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl("https://github.com/login/oauth/")
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(UserRestService.class);
    }

    @NonNull public static RestService getRestService() {
        return createService(RestService.class);
    }

    @NonNull public static RestService getRestService(@NonNull Gson gson) {
        return createService(RestService.class, gson);
    }

    @NonNull public static UserRestService getUserRestService() {
        return createService(UserRestService.class);
    }

    @NonNull public static UserRestService getUserRestService(@NonNull Gson gson) {
        return createService(UserRestService.class, gson);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service, String baseUrl) {
        return getRetrofit(baseUrl).create(service);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service) {
        return getRetrofit().create(service);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service, @NonNull Gson gson) {
        return getRetrofit(gson).create(service);
    }

    @NonNull private static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new PaginationInterceptor())
                .addInterceptor(chain -> {
//                    String[] headers = {
////                            "application/vnd.github.html+json",
//                            "application/vnd.github.raw+json"
//                    };
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/json")
                            .method(original.method(), original.body());
                    if (!InputHelper.isEmpty(PrefGetter.getToken())) {
                        requestBuilder.header("Authorization", "token " + PrefGetter.getToken());
                    }
//                    if (original.header("Accept") == null) {
//                        requestBuilder.addHeader("Accept", TextUtils.join(",", headers));
//                    }
                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    @NonNull private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .create();
        }
        return gson;
    }

    public static long downloadFile(@NonNull Context context, @NonNull String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(context.getString(R.string.downloading_file));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }
}
