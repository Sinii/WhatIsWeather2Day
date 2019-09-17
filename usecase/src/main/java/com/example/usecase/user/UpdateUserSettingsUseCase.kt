package com.example.usecase.user

import com.example.base.usecase.BaseUseCase
import com.example.repository.user.SetUserPreferencesRepository
import com.example.user.UserPreferences
import javax.inject.Inject

class UpdateUserSettingsUseCase
@Inject constructor(
    private val setUserPreferencesRepository: SetUserPreferencesRepository
) : BaseUseCase<UpdateUserSettingsUseCase.Params, UpdateUserSettingsUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        setUserPreferencesRepository
            .doWork(SetUserPreferencesRepository.Params(params.userPreferences))
        return Result()
    }

    class Params(val userPreferences: UserPreferences)
    class Result

}