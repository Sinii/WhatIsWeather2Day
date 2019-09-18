package com.example.repository.user

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base.repository.BaseRepository
import com.example.livedata.FloatPreferencesLiveData
import com.example.livedata.StringPreferencesLiveData
import com.example.user.UserPreferences
import com.example.user.UserPreferences.Companion.HUMIDITY_MAX_DEFAULT
import com.example.user.UserPreferences.Companion.HUMIDITY_MIN_DEFAULT
import com.example.user.UserPreferences.Companion.LOCATION_DEFAULT
import com.example.user.UserPreferences.Companion.TEMPERATURE_MAX_DEFAULT
import com.example.user.UserPreferences.Companion.TEMPERATURE_MIN_DEFAULT
import com.example.user.UserPreferences.Companion.WIND_MAX_DEFAULT
import com.example.user.UserPreferences.Companion.WIND_MIN_DEFAULT
import javax.inject.Inject

class UserPreferencesRepository
@Inject constructor(
    private val sp: SharedPreferences
) : BaseRepository<UserPreferencesRepository.Params, UserPreferencesRepository.Result>() {
    override suspend fun doWork(params: Params): Result {
        val mediatorLiveData = MediatorLiveData<UserPreferences>()
        mediatorLiveData.addSource(
            StringPreferencesLiveData(sp, LOCATION_PREF, LOCATION_DEFAULT)
        ) { location ->
            mediatorLiveData.transformAndUpdatePreference { this.location = location }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, TEMPERATURE_MIN_PREF, TEMPERATURE_MIN_DEFAULT)
        ) { temperatureMin ->
            mediatorLiveData.transformAndUpdatePreference { this.temperatureMin = temperatureMin }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, TEMPERATURE_MAX_PREF, TEMPERATURE_MAX_DEFAULT)
        ) { temperatureMax ->
            mediatorLiveData.transformAndUpdatePreference { this.temperatureMax = temperatureMax }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, HUMIDITY_MIN_PREF, HUMIDITY_MIN_DEFAULT)
        ) { humidityMin ->
            mediatorLiveData.transformAndUpdatePreference { this.humidityMin = humidityMin }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, HUMIDITY_MAX_PREF, HUMIDITY_MAX_DEFAULT)
        ) { humidityMax ->
            mediatorLiveData.transformAndUpdatePreference { this.humidityMax = humidityMax }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, WIND_SPEED_MIN_PREF, WIND_MIN_DEFAULT)
        ) { windMin ->
            mediatorLiveData.transformAndUpdatePreference { this.windSpeedMin = windMin }
        }
        mediatorLiveData.addSource(
            FloatPreferencesLiveData(sp, WIND_SPEED_MAX_PREF, WIND_MAX_DEFAULT)
        ) { windMax ->
            mediatorLiveData.transformAndUpdatePreference { this.windSpeedMax = windMax }
        }
        return Result(mediatorLiveData)
    }

    private inline fun MediatorLiveData<UserPreferences>?.transformAndUpdatePreference(block: UserPreferences.() -> Unit) {
        val prefs = this?.value ?: UserPreferences()
        prefs.apply(block)
        this?.value = prefs
    }

    class Params
    class Result(val userPreferences: LiveData<UserPreferences>)

    companion object {
        const val LOCATION_PREF = "location prefs"
        const val TEMPERATURE_MIN_PREF = "temp min prefs"
        const val TEMPERATURE_MAX_PREF = "temp max prefs"
        const val HUMIDITY_MIN_PREF = "humidity min prefs"
        const val HUMIDITY_MAX_PREF = "humidity max prefs"
        const val WIND_SPEED_MIN_PREF = "wind speed min prefs"
        const val WIND_SPEED_MAX_PREF = "wind speed max prefs"
    }
}