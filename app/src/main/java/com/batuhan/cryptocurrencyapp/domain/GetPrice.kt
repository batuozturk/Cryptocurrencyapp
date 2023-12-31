package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetPrice @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val id: String)

    suspend operator fun invoke(params: Params): Result<CryptoCurrencyItem?> {
        return runCatching {
            Result.Success(repository.getPrice(params.id))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
