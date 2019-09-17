package com.example.di.module

import com.anton.featureweather.di.WeatherFeatureFragmentBuildersModule
import com.anton.featureweather.di.WeatherFeatureViewModelModule
import com.example.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        WeatherFeatureViewModelModule::class
    ]
)
abstract class MainActivityModule {

    @ContributesAndroidInjector(
        modules = [
            WeatherFeatureFragmentBuildersModule::class
        ]
    )

    abstract fun contributeMainActivity(): MainActivity

}