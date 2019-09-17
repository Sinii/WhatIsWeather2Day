package com.example.di.module

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
}