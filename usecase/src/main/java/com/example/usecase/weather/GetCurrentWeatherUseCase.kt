package com.example.usecase.weather

import com.example.base.usecase.BaseUseCase
import com.example.repository.weather.WeatherRepository
import com.example.weather.Weather
import javax.inject.Inject

class GetCurrentWeatherUseCase
@Inject constructor(
    private val weatherRepository: WeatherRepository
) : BaseUseCase<GetCurrentWeatherUseCase.Params, GetCurrentWeatherUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        return Result(
            weatherRepository
                .doWork(WeatherRepository.Params(params.location))
                .weather
        )
    }

    class Params(val location: String)
    class Result(val weather: Weather?)
}