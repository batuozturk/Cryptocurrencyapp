package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.RatesResponse
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetRates @Inject constructor(private val repository: CryptoCurrencyRepository) {

    suspend operator fun invoke(): Result<RatesResponse> {
        return runCatching {
            Result.Success(repository.getRates())
        }.getOrElse {
            Result.Error(it)
        }
    }
}
