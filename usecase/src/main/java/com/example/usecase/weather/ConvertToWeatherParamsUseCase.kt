package com.example.usecase.weather

import com.example.base.usecase.BaseUseCase
import com.example.weather.Weather
import java.text.NumberFormat
import javax.inject.Inject

class ConvertToWeatherParamsUseCase
@Inject constructor() : BaseUseCase<
        ConvertToWeatherParamsUseCase.Params,
        ConvertToWeatherParamsUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        val numberFormatInst = NumberFormat.getInstance()
        return Result(
            "\n\n Temperature = ${numberFormatInst.format(params.weather.main.temp)} " +
                    "\n Humidity = ${numberFormatInst.format(params.weather.main.humidity)} " +
                    "\n Wind speed = ${numberFormatInst.format(params.weather.wind.speed)}"
        )
    }

    class Params(val weather: Weather)
    class Result(val weatherParams: String)
}