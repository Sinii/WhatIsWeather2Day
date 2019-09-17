package com.anton.featureweather.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.usecase.user.GetUserSettingsUseCase
import com.example.usecase.user.UpdateUserSettingsUseCase
import com.example.user.UserPreferences
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPreferencesViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockkStatic(Dispatchers::class)
        every {
            Dispatchers.IO
        } returns Dispatchers.Main
    }

    @Test
    fun `check weather preferences`() {
        val getUserSettingsUseCase = mockkClass(GetUserSettingsUseCase::class)
        val updateUserSettingsUseCase = mockkClass(UpdateUserSettingsUseCase::class)
        val prefsLiveData = MutableLiveData<UserPreferences>()
        val userPrefs = UserPreferences()
        every {
            runBlocking {
                getUserSettingsUseCase.doWork(any())
            }
        } returns GetUserSettingsUseCase.Result(prefsLiveData)
        every {
            runBlocking {
                updateUserSettingsUseCase.doWork(any())
            }
        } returns UpdateUserSettingsUseCase.Result()

        val vm = UserPreferencesViewModel(
            getUserSettingsUseCase,
            updateUserSettingsUseCase
        )
        vm.doAutoMainWork()
        vm.userPreferences.observeForever {}
        prefsLiveData.postValue(userPrefs)
        Assert.assertTrue(
            "current location = ${vm.location.value}",
            vm.location.value == userPrefs.location
        )
        Assert.assertTrue(
            "current temp = ${vm.temperatureMax.value}",
            vm.temperatureMax.value?.toFloat() == userPrefs.temperatureMax
        )
        Assert.assertTrue(
            "current temp = ${vm.temperatureMin.value}",
            vm.temperatureMin.value?.toFloat() == userPrefs.temperatureMin
        )
    }
}