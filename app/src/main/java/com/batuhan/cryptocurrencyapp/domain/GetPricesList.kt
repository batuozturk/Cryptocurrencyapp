package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyResponseItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetPricesList @Inject constructor(private val repository: CryptoCurrencyRepository) {

    suspend operator fun invoke(): Result<List<CryptoCurrencyResponseItem>> {
        return runCatching {
            Result.Success(repository.getPricesList().data ?: listOf())
        }.getOrElse {
            Result.Error(it)
        }
    }
}
