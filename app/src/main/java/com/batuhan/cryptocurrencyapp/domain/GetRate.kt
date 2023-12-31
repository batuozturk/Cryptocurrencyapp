package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetRate @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val id: String)

    suspend operator fun invoke(params: Params): Result<RateItem> {
        return runCatching {
            Result.Success(repository.getRate(params.id))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
