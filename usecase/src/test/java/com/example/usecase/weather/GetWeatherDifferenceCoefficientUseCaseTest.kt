package com.example.usecase.weather

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetWeatherDifferenceCoefficientUseCaseTest {
    @Test
    fun `check default behavior`() {
        val getCurrentWeatherUseCase = GetWeatherDifferenceCoefficientUseCase()
        val min = 1f
        val max = 2f
        val value = 3f
        runBlocking {
            val result = getCurrentWeatherUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(value, min, max)
                ).coefficient

            Assert.assertTrue(
                "result = $result",
                20 == result
            )
        }
    }
    @Test
    fun `check good coefficient`() {
        val getCurrentWeatherUseCase = GetWeatherDifferenceCoefficientUseCase()
        val min = 1f
        val max = 2f
        val value = 1.5f
        runBlocking {
            val result = getCurrentWeatherUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(value, min, max)
                ).coefficient

            Assert.assertTrue(
                "result = $result",
                0 == result
            )
        }
    }
    @Test
    fun `check middle coefficient`() {
        val getCurrentWeatherUseCase = GetWeatherDifferenceCoefficientUseCase()
        val min = 1f
        val max = 2f
        val value = 2.5f
        runBlocking {
            val result = getCurrentWeatherUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(value, min, max)
                ).coefficient

            Assert.assertTrue(
                "result = $result",
                10 == result
            )
        }
    }

    @Test
    fun `check different middle coefficient`() {
        val getCurrentWeatherUseCase = GetWeatherDifferenceCoefficientUseCase()
        val min = 1f
        val max = 2f
        val value = .5f
        runBlocking {
            val result = getCurrentWeatherUseCase
                .doWork(
                    GetWeatherDifferenceCoefficientUseCase.Params(value, min, max)
                ).coefficient

            Assert.assertTrue(
                "result = $result",
                10 == result
            )
        }
    }
}