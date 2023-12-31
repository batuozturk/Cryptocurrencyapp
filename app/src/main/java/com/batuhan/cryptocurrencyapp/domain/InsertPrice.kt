package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class InsertPrice @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val item: CryptoCurrencyItem)

    suspend operator fun invoke(params: Params): Result<Unit> {
        return runCatching {
            Result.Success(repository.insertPrice(params.item))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
