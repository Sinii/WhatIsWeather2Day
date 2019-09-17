package com.example.usecase.weather

import com.example.base.usecase.BaseUseCase
import com.example.weather.Weather
import javax.inject.Inject

class ConvertToWeatherParamsUseCase
@Inject constructor() : BaseUseCase<
        ConvertToWeatherParamsUseCase.Params,
        ConvertToWeatherParamsUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        return Result(
            "\n\n Temperature = ${params.weather.main.temp} \n Humidity = ${params.weather.main.humidity} \n Wind speed = ${params.weather.wind.speed}"
        )
    }

    class Params(val weather: Weather)
    class Result(val weatherParams: String)
}