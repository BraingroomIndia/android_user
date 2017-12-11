package com.braingroom.user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.braingroom.user.BuildConfig;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.CustomInterceptor;
import com.braingroom.user.model.UserApiService;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final UserApplication application;
    String mBaseUrl;

    public AppModule(UserApplication application, String baseUrl) {
        this.application = application;
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public UserApplication provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }


    @Provides
    @Singleton
    Cache provideOkHttpCache() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Gson gson) {
        OkHttpClient.Builder builder;
        CustomInterceptor customInterceptor = new CustomInterceptor(gson);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            builder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                    .addInterceptor(customInterceptor)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .cache(cache);
        } else {
            builder = new OkHttpClient.Builder()
                    .addInterceptor(customInterceptor)
                    .cache(cache);

        }

        builder = builder.connectTimeout(100, TimeUnit.SECONDS);
        builder = builder.readTimeout(100, TimeUnit.SECONDS);
        builder = builder.writeTimeout(100, TimeUnit.SECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("api")
    UserApiService provideUserApiService(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit.create(UserApiService.class);
    }


    @Provides
    @Singleton
    @Named("defaultPref")
    SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    @Named("defaultPrefEditor")
    SharedPreferences.Editor providesSharedPreferenceseditor() {
        return providesSharedPreferences().edit();
    }


    @Provides
    @Singleton
    Picasso providesPicasso() {
        if (BuildConfig.DEBUG) {
            Picasso picasso = Picasso.with(application);
            picasso.setIndicatorsEnabled(true);
            return picasso;
        } else {
            return Picasso.with(application);
        }
    }

}
