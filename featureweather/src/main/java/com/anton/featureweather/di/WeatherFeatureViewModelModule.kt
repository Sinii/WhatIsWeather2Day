package com.anton.featureweather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anton.featureweather.ui.UserPreferencesViewModel
import com.anton.featureweather.ui.WeatherViewModel
import com.example.base.di.ViewModelFactory
import com.example.base.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WeatherFeatureViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    internal abstract fun bindWeatherViewModel(viewModel: WeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserPreferencesViewModel::class)
    internal abstract fun bindUserPreferencesViewModel(viewModel: UserPreferencesViewModel): ViewModel

    //Add more ViewModels here
}