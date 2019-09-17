package com.example.usecase.weather

import com.example.base.usecase.BaseUseCase
import javax.inject.Inject

class GetWeatherDifferenceCoefficientUseCase
@Inject constructor() : BaseUseCase<
        GetWeatherDifferenceCoefficientUseCase.Params,
        GetWeatherDifferenceCoefficientUseCase.Result>() {
    override suspend fun doWork(params: Params): Result {
        var result = 0
        if (params.value !in params.min..params.max) {
            if (params.value < params.min)
                result =
                    (MAX_DIFFERENCE_COEFFICIENT * (params.min - params.value) / (params.max - params.min)).toInt()
            if (params.value > params.max)
                result =
                    (MAX_DIFFERENCE_COEFFICIENT * (params.value - params.max) / (params.max - params.min)).toInt()
        }
        return Result(result)
    }

    class Params(val value: Float, val min: Float, val max: Float)
    class Result(val coefficient: Int)

    companion object {
        const val MAX_DIFFERENCE_COEFFICIENT = 20
    }
}