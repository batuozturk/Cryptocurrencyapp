package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetFavoriteRate @Inject constructor(private val repository: CryptoCurrencyRepository) {

    suspend operator fun invoke(): Result<RateItem?> {
        return runCatching {
            Result.Success(repository.getFavoriteRate())
        }.getOrElse {
            Result.Error(it)
        }
    }
}
