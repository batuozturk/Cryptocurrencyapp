package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetPriceFlow @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val id: String)

    operator fun invoke(params: Params) = repository.getPriceFlow(params.id)
}
