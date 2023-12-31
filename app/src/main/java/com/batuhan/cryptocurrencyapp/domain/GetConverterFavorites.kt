package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetConverterFavorites @Inject constructor(private val repository: CryptoCurrencyRepository) {

    operator fun invoke() = repository.getConverterFavorites()
}
