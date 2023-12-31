package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.HistoryItemResponse
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetPriceHistory @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val id: String, val interval: String)

    suspend operator fun invoke(params: Params): Result<HistoryItemResponse> {
        return runCatching {
            Result.Success(repository.getHistory(params.id, params.interval))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
