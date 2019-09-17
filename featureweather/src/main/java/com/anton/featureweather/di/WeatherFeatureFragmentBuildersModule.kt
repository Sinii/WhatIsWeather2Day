package com.anton.featureweather.di


import com.anton.featureweather.ui.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WeatherFeatureFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeWeatherFragment(): WeatherFragment

    //Add more Fragments here

}
