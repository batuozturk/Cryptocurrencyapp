package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.MarketItemResponse
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetExchanges @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val baseId: String)

    suspend operator fun invoke(params: Params): Result<MarketItemResponse> {
        return runCatching {
            Result.Success(repository.getExchanges(params.baseId))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
