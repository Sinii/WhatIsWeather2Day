package com.example.usecase.user

import androidx.lifecycle.LiveData
import com.example.base.usecase.BaseUseCase
import com.example.repository.user.UserPreferencesRepository
import com.example.user.UserPreferences
import javax.inject.Inject

class GetUserSettingsUseCase
@Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseUseCase<GetUserSettingsUseCase.Params, GetUserSettingsUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        return Result(
            userPreferencesRepository
                .doWork(UserPreferencesRepository.Params())
                .userPreferences
        )
    }

    class Params
    class Result(val userPreference: LiveData<UserPreferences>)

}