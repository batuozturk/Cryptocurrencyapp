package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.CandleItemResponse
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetCandles @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val interval: String, val baseId: String)

    suspend operator fun invoke(params: Params): Result<CandleItemResponse> {
        return runCatching {
            Result.Success(repository.getCandles(params.interval, params.baseId))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
