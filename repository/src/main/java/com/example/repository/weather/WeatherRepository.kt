package com.example.repository.weather

import com.example.base.repository.BaseRepository
import com.example.network.retrofit.RetrofitWeather
import com.example.weather.Weather
import javax.inject.Inject

class WeatherRepository
@Inject constructor(
    private val retrofitWeather: RetrofitWeather
) : BaseRepository<WeatherRepository.Params, WeatherRepository.Result>() {
    override suspend fun doWork(params: Params): Result {
        return Result(
            retrofitWeather.getWeatherByLocationAsync(
                params.location,
                API_KEY,
                METRIC_UNITS
            ).body()
        )
    }

    class Params(val location: String)
    class Result(val weather: Weather?)

    companion object {
        const val API_KEY = "d69dd26251033117ac8726a0976cea9b"
        const val METRIC_UNITS = "metric"
    }
}