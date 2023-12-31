package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.RatesResponseItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class InsertRates @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val list: List<RatesResponseItem>)

    suspend operator fun invoke(params: Params): Result<Unit> {
        return runCatching {
            Result.Success(repository.insertRates(params.list))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
