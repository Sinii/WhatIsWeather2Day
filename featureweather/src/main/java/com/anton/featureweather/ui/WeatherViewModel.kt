package com.anton.featureweather.ui

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.BaseViewModel
import com.example.usecase.user.GetUserSettingsUseCase
import com.example.usecase.weather.GetCurrentWeatherUseCase
import com.example.usecase.weather.GetWeatherTypeUseCase
import com.example.user.UserPreferences
import com.example.utils.dLog
import com.example.weather.Weather
import kotlinx.coroutines.Job
import javax.inject.Inject

class WeatherViewModel
@Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeatherTypeUseCase: GetWeatherTypeUseCase
) : BaseViewModel() {
    val loadingVisibility = MutableLiveData(GONE)
    val weatherType = MutableLiveData("")
    var userPreferences = MediatorLiveData<UserPreferences>()
    private lateinit var userPreferencesSource: LiveData<UserPreferences>

    private var currentWeather: Weather? = null
    private var requestSettings: Job? = null
    private var requestWeather: Job? = null

    override fun doAutoMainWork() {
        requestSettings?.cancel()
        requestSettings = doWork {
            val result = getUserSettingsUseCase
                .doWork(GetUserSettingsUseCase.Params())

            doWorkInMainThread {
                userPreferencesSource = result.userPreference
                userPreferences.addSource(userPreferencesSource) {
                    "preferences changed = ${userPreferencesSource.value}".dLog()
                    userPreferences.value = userPreferencesSource.value
                    firePreferencesChanged()
                }
            }
        }
    }

    private fun firePreferencesChanged() {
        userPreferences.value?.location?.let {
            loadingVisibility.postValue(VISIBLE)
            requestWeather?.cancel()
            requestWeather = doWork {
                currentWeather = getCurrentWeatherUseCase
                    .doWork(GetCurrentWeatherUseCase.Params(it))
                    .weather
                fireWeatherChanged()
                loadingVisibility.postValue(GONE)
            }
        }
    }

    private suspend fun fireWeatherChanged() {
        val preferences = userPreferences.value
        val weather = currentWeather
        val type = getWeatherTypeUseCase
            .doWork(GetWeatherTypeUseCase.Params(preferences, weather))
            .weatherType
        weatherType.postValue(type)
    }

    override fun clearSources() {
        userPreferences.removeSource(userPreferencesSource)
    }
}