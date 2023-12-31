package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.data.model.FilterType
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import javax.inject.Inject

class GetPrices @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val filterType: FilterType, val filterText: String)

    operator fun invoke(params: Params) = repository.getPrices(params.filterType, params.filterText)
}
