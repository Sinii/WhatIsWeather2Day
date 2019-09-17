package com.example.usecase.weather

import com.example.base.usecase.BaseUseCase
import com.example.user.UserPreferences
import com.example.weather.Weather
import javax.inject.Inject

class GetWeatherTypeUseCase
@Inject constructor(
    private val getWeatherDifferenceCoefficientUseCase: GetWeatherDifferenceCoefficientUseCase,
    private val convertToWeatherParamsUseCase:ConvertToWeatherParamsUseCase
) : BaseUseCase<GetWeatherTypeUseCase.Params, GetWeatherTypeUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        var weatherCoefficient = 100
        val weatherType: String
        if (params.weather != null && params.userPreferences != null) {
            weatherCoefficient -= getWeatherDifferenceCoefficientUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(
                        params.weather.main.temp,
                        params.userPreferences.temperatureMin,
                        params.userPreferences.temperatureMax
                    )
                )
                .coefficient
            weatherCoefficient -= getWeatherDifferenceCoefficientUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(
                        params.weather.main.humidity,
                        params.userPreferences.humidityMin,
                        params.userPreferences.humidityMax
                    )
                )
                .coefficient
            weatherCoefficient -= getWeatherDifferenceCoefficientUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(
                        params.weather.wind.speed,
                        params.userPreferences.windSpeedMin,
                        params.userPreferences.windSpeedMax
                    )
                )
                .coefficient
            val weatherParams = convertToWeatherParamsUseCase
                .doWork(ConvertToWeatherParamsUseCase.Params(params.weather))
                .weatherParams
            weatherType = when (weatherCoefficient) {
                in 0..50 -> "Weather in ${params.userPreferences.location} is bad"
                in 51..80 -> "Weather in ${params.userPreferences.location} is ok"
                in 81..100 -> "Weather in ${params.userPreferences.location} is good"
                else -> "Strange weather"
            } + weatherParams
        } else {
            weatherType = "Try different location"
        }

        return Result(weatherType)
    }

    class Params(val userPreferences: UserPreferences?, val weather: Weather?)
    class Result(val weatherType: String)
}