package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetRatesFromDB @Inject constructor(private val repository: CryptoCurrencyRepository) {

    suspend operator fun invoke(): Result<List<RateItem>> {
        return runCatching {
            Result.Success(repository.getRatesFromDB())
        }.getOrElse {
            Result.Error(it)
        }
    }
}
