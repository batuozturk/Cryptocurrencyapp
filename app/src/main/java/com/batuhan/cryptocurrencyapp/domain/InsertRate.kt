package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class InsertRate @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val item: RateItem)

    suspend operator fun invoke(params: Params): Result<Unit> {
        return runCatching {
            Result.Success(repository.insertRate(params.item))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
