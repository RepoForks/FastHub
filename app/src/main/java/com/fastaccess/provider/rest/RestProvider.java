package com.fastaccess.provider.rest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.fastaccess.R;
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

    @NonNull private static Retrofit getRetrofit(boolean isRaw) {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient(isRaw))
                .addConverterFactory(!isRaw ? GsonConverterFactory.create(getGson()) : new StringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofitString() {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient(false, true))
                .addConverterFactory(new StringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofit() {
        return getRetrofit(false);
    }

    @NonNull private static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                .client(getHttpClient(false))
                .addConverterFactory(new StringConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull private static Retrofit getRetrofit(@NonNull Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .client(getHttpClient(false))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
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

    @NonNull public static <S> S createService(@NonNull Class<S> service, boolean isRaw) {
        return getRetrofit(isRaw).create(service);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service) {
        return createService(service, false);
    }

    @NonNull public static <S> S createStringService(@NonNull Class<S> service) {
        return getRetrofitString().create(service);
    }

    @NonNull public static <S> S createService(@NonNull Class<S> service, @NonNull Gson gson) {
        return getRetrofit(gson).create(service);
    }

    @NonNull private static OkHttpClient getHttpClient(boolean isRaw) {
        return getHttpClient(isRaw, false);
    }

    @NonNull private static OkHttpClient getHttpClient(boolean isRaw, boolean isHtml) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new PaginationInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", getAccept(isRaw, isHtml))
                            .header("Content-type", getContentType(isRaw, isHtml));
                    if (!InputHelper.isEmpty(PrefGetter.getToken())) {
                        requestBuilder.header("Authorization", "token " + PrefGetter.getToken());
                    }
                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    @NonNull private static String getAccept(boolean isRaw, boolean isHtml) {
        return isHtml ? "application/vnd.github.html"
                      : !isRaw ? "application/vnd.github.v3+json"
                               : "application/vnd.github.VERSION.raw";
    }

    @NonNull private static String getContentType(boolean isRaw, boolean isHtml) {
        return isHtml ? "application/vnd.github.html"
                      : !isRaw ? "application/vnd.github.v3+json"
                               : "application/vnd.github.VERSION.raw; charset=utf-8";
    }

    @NonNull public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();
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

    public static int getErrorCode(Throwable throwable) {
        if (throwable instanceof HttpException) {
            return ((HttpException) throwable).code();

        }
        return -1;
    }
}
