package com.example.repository.user

import android.content.SharedPreferences
import com.example.base.repository.BaseRepository
import com.example.user.UserPreferences
import javax.inject.Inject


class SetUserPreferencesRepository
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) : BaseRepository<SetUserPreferencesRepository.Params, SetUserPreferencesRepository.Result>() {
    override suspend fun doWork(params: Params): Result {
        sharedPreferences.edit()
            .putString(UserPreferencesRepository.LOCATION_PREF, params.userPreferences.location)
            .putFloat(
                UserPreferencesRepository.TEMPERATURE_MIN_PREF,
                params.userPreferences.temperatureMin
            )
            .putFloat(
                UserPreferencesRepository.TEMPERATURE_MAX_PREF,
                params.userPreferences.temperatureMax
            )
            .putFloat(
                UserPreferencesRepository.HUMIDITY_MIN_PREF,
                params.userPreferences.humidityMin
            )
            .putFloat(
                UserPreferencesRepository.HUMIDITY_MAX_PREF,
                params.userPreferences.humidityMax
            )
            .putFloat(
                UserPreferencesRepository.WIND_SPEED_MIN_PREF,
                params.userPreferences.windSpeedMin
            )
            .putFloat(
                UserPreferencesRepository.WIND_SPEED_MAX_PREF,
                params.userPreferences.windSpeedMax
            )
            .apply()
        return Result()
    }

    class Params(val userPreferences: UserPreferences)
    class Result
}