package com.example.usecase.weather

import com.example.repository.weather.WeatherRepository
import com.example.weather.MainWeather
import com.example.weather.Weather
import com.example.weather.Wind
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetCurrentWeatherUseCaseTest {
    @Test
    fun `check default behavior`() {
        val weather = Weather(MainWeather(0f, 1f, 2f), Wind(3f))
        val repository = mockkClass(WeatherRepository::class)
        every {
            runBlocking {
                repository.doWork(any())
            }
        } returns WeatherRepository.Result(weather)
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(repository)
        runBlocking {
            val result = getCurrentWeatherUseCase
                .doWork(GetCurrentWeatherUseCase.Params(""))
                .weather
            Assert.assertTrue(
                weather == result
            )
        }
    }
}