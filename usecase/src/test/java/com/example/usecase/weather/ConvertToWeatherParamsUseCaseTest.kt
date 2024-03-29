package com.example.usecase.weather

import com.example.weather.MainWeather
import com.example.weather.Weather
import com.example.weather.Wind
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


class ConvertToWeatherParamsUseCaseTest {

    @Test
    fun `check default behavior`() {
        val convertToWeatherParamsUseCase = ConvertToWeatherParamsUseCase()
        val weather = Weather(MainWeather(0f, 1f, 2f), Wind(3f))
        runBlocking {
            val result = convertToWeatherParamsUseCase
                .doWork(ConvertToWeatherParamsUseCase.Params(weather))
                .weatherParams
            Assert.assertTrue( " result = $result",
                result == "\n\n" +
                        " Temperature = 0 \n" +
                        " Humidity = 2 \n" +
                        " Wind speed = 3"
            )
        }
    }
}