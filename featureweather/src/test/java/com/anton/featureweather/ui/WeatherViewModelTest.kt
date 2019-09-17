package com.anton.featureweather.ui

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.usecase.user.GetUserSettingsUseCase
import com.example.usecase.weather.GetCurrentWeatherUseCase
import com.example.usecase.weather.GetWeatherTypeUseCase
import com.example.user.UserPreferences
import com.example.weather.MainWeather
import com.example.weather.Weather
import com.example.weather.Wind
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockkStatic(Dispatchers::class)
        every {
            Dispatchers.IO
        } returns Dispatchers.Main
    }

    @Test
    fun `check no loading state`() {
        val getUserSettingsUseCase = mockkClass(GetUserSettingsUseCase::class)
        val getCurrentWeatherUseCase = mockkClass(GetCurrentWeatherUseCase::class)
        val getWeatherTypeUseCase = mockkClass(GetWeatherTypeUseCase::class)
        val weatherType = "test"
        val prefsLiveData = MutableLiveData<UserPreferences>()
        every {
            runBlocking {
                getUserSettingsUseCase.doWork(any())
            }
        } returns GetUserSettingsUseCase.Result(prefsLiveData)
        every {
            runBlocking {
                getCurrentWeatherUseCase.doWork(any())
            }
        } returns GetCurrentWeatherUseCase.Result(Weather(MainWeather(0f, 1f, 2f), Wind(3f)))
        every {
            runBlocking {
                getWeatherTypeUseCase.doWork(any())
            }
        } returns GetWeatherTypeUseCase.Result(weatherType)
        val vm = WeatherViewModel(
            getUserSettingsUseCase,
            getCurrentWeatherUseCase,
            getWeatherTypeUseCase
        )
        vm.doAutoMainWork()
        Assert.assertTrue(vm.loadingVisibility.value == View.GONE)
    }

    @Test
    fun `check weather type`() {
        val getUserSettingsUseCase = mockkClass(GetUserSettingsUseCase::class)
        val getCurrentWeatherUseCase = mockkClass(GetCurrentWeatherUseCase::class)
        val getWeatherTypeUseCase = mockkClass(GetWeatherTypeUseCase::class)
        val weatherType = "test"
        val prefsLiveData = MutableLiveData<UserPreferences>()
        every {
            runBlocking {
                getUserSettingsUseCase.doWork(any())
            }
        } returns GetUserSettingsUseCase.Result(prefsLiveData)
        every {
            runBlocking {
                getCurrentWeatherUseCase.doWork(any())
            }
        } returns GetCurrentWeatherUseCase.Result(Weather(MainWeather(0f, 1f, 2f), Wind(3f)))
        every {
            runBlocking {
                getWeatherTypeUseCase.doWork(any())
            }
        } returns GetWeatherTypeUseCase.Result(weatherType)
        val vm = WeatherViewModel(
            getUserSettingsUseCase,
            getCurrentWeatherUseCase,
            getWeatherTypeUseCase
        )
        vm.doAutoMainWork()
        vm.userPreferences.observeForever {}
        prefsLiveData.postValue(UserPreferences())
        Assert.assertTrue(
            "current weather = ${vm.weatherType.value}",
            vm.weatherType.value == weatherType
        )
    }
}