package com.example.network.di

import com.example.network.retrofit.RetrofitWeather
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class WeatherFeatureNetworkModule {

    @Provides
    @Singleton
    fun provideWeatherRetrofit(retrofit: Retrofit): RetrofitWeather = retrofit
        .create(RetrofitWeather::class.java)
}