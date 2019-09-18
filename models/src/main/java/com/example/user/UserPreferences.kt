package com.example.user

data class UserPreferences(
    var temperatureMin: Float = TEMPERATURE_MIN_DEFAULT,
    var temperatureMax: Float = TEMPERATURE_MAX_DEFAULT,
    var humidityMin: Float = HUMIDITY_MIN_DEFAULT,
    var humidityMax: Float = HUMIDITY_MAX_DEFAULT,
    var windSpeedMin: Float = WIND_MIN_DEFAULT,
    var windSpeedMax: Float = WIND_MAX_DEFAULT,
    var location: String = LOCATION_DEFAULT
) {
    companion object {
        const val TEMPERATURE_MIN_DEFAULT = -5f
        const val TEMPERATURE_MAX_DEFAULT = 30f
        const val HUMIDITY_MIN_DEFAULT = 50f
        const val HUMIDITY_MAX_DEFAULT = 70f
        const val WIND_MIN_DEFAULT = 0f
        const val WIND_MAX_DEFAULT = 2f
        const val LOCATION_DEFAULT = "Amsterdam"

        val TEMPERATURE_RANGE_DEFAULT = -60f to 60f
        val HUMIDITY_RANGE_DEFAULT = 10f to 100f
        val WIND_RANGE_DEFAULT = 0f to 10f
    }
}