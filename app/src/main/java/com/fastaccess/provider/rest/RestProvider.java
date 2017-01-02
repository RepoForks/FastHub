package com.fastaccess.provider.rest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.R;
import com.fastaccess.data.rest.service.UserRestService;
import com.fastaccess.helper.FileHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
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
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kosh on 04 Nov 2016, 2:11 PM
 */

public class RestProvider {

    public static final int PAGE_SIZE = 30;
    public static final String REST_URL = "https://api.github.com/";

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

    @NonNull private static Retrofit getRetrofitString() {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient())
                .addConverterFactory(new StringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofit(String baseUrl, boolean isGson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                .client(getHttpClient())
                .addConverterFactory(!isGson ? new StringConverter() : GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull public static UserRestService getUserRestService() {
        return createService(UserRestService.class);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service, String baseUrl, boolean isGson) {
        return getRetrofit(baseUrl, isGson).create(service);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service) {
        return getRetrofit().create(service);
    }

    @NonNull public static <S> S createStringService(@NonNull Class<S> service) {
        return getRetrofitString().create(service);
    }

    @NonNull private static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new PaginationInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();
                    if (!InputHelper.isEmpty(PrefGetter.getToken())) {
                        requestBuilder.header("Authorization", "token " + PrefGetter.getToken());
                    }
                    requestBuilder.addHeader("Accept", "application/vnd.github.v3+json")
                            .addHeader("Content-type", "application/vnd.github.v3+json");
                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    @NonNull public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();
    }

    public static long downloadFile(@NonNull Context context, @NonNull String url, @Nullable String fileName) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        Logger.e(FileHelper.getDownloadDirectory(), fileName);
        if (!InputHelper.isEmpty(fileName)) {
            request.setDestinationInExternalPublicDir(FileHelper.getDownloadDirectory(), fileName);
            request.setDescription(String.format("%s %s", context.getString(R.string.downloading), fileName));
        } else {
            request.setDescription(String.format("%s %s", context.getString(R.string.downloading), url));
        }
        request.setTitle(context.getString(R.string.downloading_file));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }

    public static long downloadFile(@NonNull Context context, @NonNull String url) {
        return downloadFile(context, url, null);
    }

    public static int getErrorCode(Throwable throwable) {
        if (throwable instanceof HttpException) {
            return ((HttpException) throwable).code();

        }
        return -1;
    }
}
