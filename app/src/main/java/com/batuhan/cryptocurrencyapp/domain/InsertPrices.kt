package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyConverterResponseItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyResponseItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class InsertPrices @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(
        val list: List<CryptoCurrencyResponseItem>? = null,
        val converterList: List<CryptoCurrencyConverterResponseItem>? = null
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        return runCatching {
            Result.Success(
                params.list?.let {
                    repository.insertPrices(it)
                } ?: run {
                    repository.insertPricesForConverter(params.converterList!!)
                }
            )
        }.getOrElse {
            Result.Error(it)
        }
    }
}
