package com.example.livedata

import android.content.SharedPreferences


class StringPreferencesLiveData(
    prefs: SharedPreferences,
    key: String,
    defValue: String
) : PreferencesLiveData<String>(prefs, key, defValue) {

    override fun getValueFromPreferences(key: String, defValue: String): String {
        return sharedPrefs.getString(key, defValue) ?: defValue
    }
}