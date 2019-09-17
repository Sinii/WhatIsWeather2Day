package com.anton.featureweather.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.BaseViewModel
import com.example.usecase.user.GetUserSettingsUseCase
import com.example.usecase.user.UpdateUserSettingsUseCase
import com.example.user.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.text.NumberFormat
import javax.inject.Inject

class UserPreferencesViewModel
@Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase
) : BaseViewModel() {
    val location = MutableLiveData<String>()
    val temperatureMin = MutableLiveData<String>()
    val temperatureMax = MutableLiveData<String>()
    val humidityMin = MutableLiveData<String>()
    val humidityMax = MutableLiveData<String>()
    val windMin = MutableLiveData<String>()
    val windMax = MutableLiveData<String>()

    var userPreferences = MediatorLiveData<UserPreferences>()

    private var updatePreferences: Job? = null
    private var getPreferences: Job? = null

    override fun doAutoMainWork() {
        getPreferences?.cancel()
        getPreferences = doWork {
            val result = getUserSettingsUseCase
                .doWork(GetUserSettingsUseCase.Params())
                .userPreference
            doWorkInMainThread {
                userPreferences.addSource(location) {
                    transformAndFirePreference { this.location = it }
                }
                userPreferences.addSource(temperatureMin) {
                    transformAndFirePreference { this.temperatureMin = it.toFloat() }
                }
                userPreferences.addSource(temperatureMax) {
                    transformAndFirePreference { this.temperatureMax = it.toFloat() }
                }
                userPreferences.addSource(humidityMin) {
                    transformAndFirePreference { this.humidityMin = it.toFloat() }
                }
                userPreferences.addSource(humidityMax) {
                    transformAndFirePreference { this.humidityMax = it.toFloat() }
                }
                userPreferences.addSource(windMin) {
                    transformAndFirePreference { this.windSpeedMin = it.toFloat() }
                }
                userPreferences.addSource(windMax) {
                    transformAndFirePreference { this.windSpeedMax = it.toFloat() }
                }
                userPreferences.addSource(result) {
                    location.postValue(it.location)
                    val numberFormatInst = NumberFormat.getInstance()
                    temperatureMin.postValue(numberFormatInst.format(it.temperatureMin))
                    temperatureMax.postValue(numberFormatInst.format(it.temperatureMax))
                    humidityMin.postValue(numberFormatInst.format(it.humidityMin))
                    humidityMax.postValue(numberFormatInst.format(it.humidityMax))
                    windMin.postValue(numberFormatInst.format(it.windSpeedMin))
                    windMax.postValue(numberFormatInst.format(it.windSpeedMax))
                    userPreferences.postValue(it)
                }
            }
        }
    }

    private inline fun transformAndFirePreference(block: UserPreferences.() -> Unit) {
        try {
            val currentPreferences = userPreferences.value ?: UserPreferences()
            fireUpdatePreferences(currentPreferences.apply(block))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun fireUpdatePreferences(userPreferences: UserPreferences) {
        updatePreferences?.cancel()
        updatePreferences = updatePreferencesJob(userPreferences)
    }

    private fun updatePreferencesJob(userPreferences: UserPreferences): Job = doWork {
        delay(DEBOUNCE_DELAY)
        updateUserSettingsUseCase
            .doWork(UpdateUserSettingsUseCase.Params(userPreferences))
    }

    override fun clearSources() {
        userPreferences.removeSource(location)
        userPreferences.removeSource(temperatureMin)
        userPreferences.removeSource(temperatureMax)
        userPreferences.removeSource(humidityMin)
        userPreferences.removeSource(humidityMax)
        userPreferences.removeSource(windMin)
        userPreferences.removeSource(windMax)
    }

    companion object {
        const val DEBOUNCE_DELAY = 1000L
    }
}