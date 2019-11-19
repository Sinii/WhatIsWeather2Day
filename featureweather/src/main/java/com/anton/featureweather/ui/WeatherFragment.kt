package com.anton.featureweather.ui

import androidx.lifecycle.Observer
import com.anton.featureweather.R
import com.anton.featureweather.databinding.FragmentWeatherBinding
import com.example.base.di.ViewModelFactory
import com.example.base.ui.BaseFragment
import kotlin.reflect.KClass


class WeatherFragment : BaseFragment<FragmentWeatherBinding, ViewModelFactory>() {

    override fun provideListOfViewModels(): Array<KClass<*>> = arrayOf(
        WeatherViewModel::class,
        UserPreferencesViewModel::class
    )

    override fun provideActionsBinding(): (FragmentWeatherBinding, Set<*>) -> Unit =
        { binding, viewModelList ->
            viewModelList.forEach { viewModel ->
                when (viewModel) {
                    is WeatherViewModel -> {
                        binding.weatherVM = viewModel
                        viewModel.userPreferences.observe(this, Observer {

                        })
                    }
                    is UserPreferencesViewModel -> {
                        binding.preferencesVM = viewModel
                        viewModel.userPreferences.observe(this, Observer {})
                    }
                }
            }
        }

    override fun provideLayout() = R.layout.fragment_weather

    override fun provideLifecycleOwner() = this
}