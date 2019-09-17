package com.example.livedata

import android.content.SharedPreferences


class FloatPreferencesLiveData(
    prefs: SharedPreferences,
    key: String,
    defValue: Float
) : PreferencesLiveData<Float>(prefs, key, defValue) {

    override fun getValueFromPreferences(key: String, defValue: Float): Float {
        return sharedPrefs.getFloat(key, defValue)
    }
}