package com.example.network.di

import com.example.constants.Preferences.Companion.CONNECT_TIME_OUT_SECONDS
import com.example.network.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class CustomRequesterModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        })
        .retryOnConnectionFailure(true)
        .connectTimeout(CONNECT_TIME_OUT_SECONDS, TimeUnit.SECONDS)
        .build()
}