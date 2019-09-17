package com.example.network.retrofit

import com.example.weather.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitWeather {

    @GET("weather")
    suspend fun getWeatherByLocationAsync(
        @Query("q") location: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Response<Weather>
}
