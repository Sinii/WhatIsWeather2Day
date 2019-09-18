package com.anton.featureweather.ui

import android.widget.SeekBar
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.BaseViewModel
import com.example.interfaces.SimpleSeekBarChangeListener
import com.example.usecase.user.GetUserSettingsUseCase
import com.example.usecase.user.UpdateUserSettingsUseCase
import com.example.user.UserPreferences
import com.example.user.UserPreferences.Companion.HUMIDITY_RANGE_DEFAULT
import com.example.user.UserPreferences.Companion.TEMPERATURE_RANGE_DEFAULT
import com.example.user.UserPreferences.Companion.WIND_RANGE_DEFAULT
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.text.NumberFormat
import javax.inject.Inject
import kotlin.math.abs

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


    val temperatureMinProgress = MutableLiveData<Int>()
    val temperatureMaxProgress = MutableLiveData<Int>()
    val humidityMinProgress = MutableLiveData<Int>()
    val humidityMaxProgress = MutableLiveData<Int>()
    val windMinProgress = MutableLiveData<Int>()
    val windMaxProgress = MutableLiveData<Int>()

    val temperatureMinChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    TEMPERATURE_RANGE_DEFAULT.first,
                    TEMPERATURE_RANGE_DEFAULT.first,
                    temperatureMax.value?.toFloat() ?: TEMPERATURE_RANGE_DEFAULT.second
                )
                temperatureMin.postValue(newValue.toString())
            }
        }
    })
    val temperatureMaxChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    temperatureMin.value?.toFloat() ?: TEMPERATURE_RANGE_DEFAULT.first,
                    TEMPERATURE_RANGE_DEFAULT.first,
                    TEMPERATURE_RANGE_DEFAULT.second
                )
                temperatureMax.postValue(newValue.toString())
            }
        }
    })
    val humidityMinChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    HUMIDITY_RANGE_DEFAULT.first,
                    HUMIDITY_RANGE_DEFAULT.first,
                    humidityMax.value?.toFloat() ?: HUMIDITY_RANGE_DEFAULT.second
                )
                humidityMin.postValue(newValue.toString())
            }
        }
    })
    val humidityMaxChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    humidityMin.value?.toFloat() ?: HUMIDITY_RANGE_DEFAULT.first,
                    HUMIDITY_RANGE_DEFAULT.first,
                    HUMIDITY_RANGE_DEFAULT.second
                )
                humidityMax.postValue(newValue.toString())
            }
        }
    })
    val windMinChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    WIND_RANGE_DEFAULT.first,
                    WIND_RANGE_DEFAULT.first,
                    windMax.value?.toFloat() ?: WIND_RANGE_DEFAULT.second
                )
                windMin.postValue(newValue.toString())
            }
        }
    })
    val windMaxChangeListener = MutableLiveData<SimpleSeekBarChangeListener>(object :
        SimpleSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                val newValue = progress.calculateValueForSeekBar(
                    windMin.value?.toFloat() ?: WIND_RANGE_DEFAULT.first,
                    WIND_RANGE_DEFAULT.first,
                    WIND_RANGE_DEFAULT.second
                )
                windMax.postValue(newValue.toString())
            }
        }
    })

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
                    temperatureMinProgress.postValue(
                        it.temperatureMin.calculateProgressForSeekBar(
                            TEMPERATURE_RANGE_DEFAULT.first,
                            TEMPERATURE_RANGE_DEFAULT.first,
                            it.temperatureMax
                        )
                    )
                    temperatureMaxProgress.postValue(
                        it.temperatureMax.calculateProgressForSeekBar(
                            it.temperatureMin,
                            TEMPERATURE_RANGE_DEFAULT.first,
                            TEMPERATURE_RANGE_DEFAULT.second
                        )
                    )
                    humidityMinProgress.postValue(
                        it.humidityMin.calculateProgressForSeekBar(
                            HUMIDITY_RANGE_DEFAULT.first,
                            HUMIDITY_RANGE_DEFAULT.first,
                            it.humidityMax
                        )
                    )
                    humidityMaxProgress.postValue(
                        it.humidityMax.calculateProgressForSeekBar(
                            it.humidityMin,
                            HUMIDITY_RANGE_DEFAULT.first,
                            HUMIDITY_RANGE_DEFAULT.second
                        )
                    )
                    windMinProgress.postValue(
                        it.windSpeedMin.calculateProgressForSeekBar(
                            WIND_RANGE_DEFAULT.first,
                            WIND_RANGE_DEFAULT.first,
                            it.windSpeedMax
                        )
                    )
                    windMaxProgress.postValue(
                        it.windSpeedMax.calculateProgressForSeekBar(
                            it.windSpeedMin,
                            WIND_RANGE_DEFAULT.first,
                            WIND_RANGE_DEFAULT.second
                        )
                    )
                    userPreferences.postValue(it)
                }
            }
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

    private fun Int.calculateValueForSeekBar(from: Float, min: Float, max: Float) =
        from + this * (max - min) / MAX_PROGRESS_SEEK_BAR

    private fun Float.calculateProgressForSeekBar(from: Float, min: Float, max: Float) =
        abs((this - from) / ((max - min) / MAX_PROGRESS_SEEK_BAR)).toInt()

    private inline fun transformAndFirePreference(block: UserPreferences.() -> Unit) {
        try {
            val currentPreferences = userPreferences.value ?: UserPreferences()
            fireUpdatePreferences(currentPreferences.apply(block))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val DEBOUNCE_DELAY = 1000L
        const val MAX_PROGRESS_SEEK_BAR = 100
    }
}