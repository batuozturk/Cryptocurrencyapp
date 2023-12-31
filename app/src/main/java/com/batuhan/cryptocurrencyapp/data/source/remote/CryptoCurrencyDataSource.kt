package com.batuhan.cryptocurrencyapp.data.source.remote

import javax.inject.Inject

class CryptoCurrencyDataSource @Inject constructor(private val cryptoCurrencyService: CryptoCurrencyService) {

    suspend fun getPrices() = cryptoCurrencyService.getPrices()

    suspend fun getHistory(id: String, interval: String) =
        cryptoCurrencyService.getHistory(id, interval)

    suspend fun getRates() = cryptoCurrencyService.getRates()

    suspend fun getExchanges(baseId: String) = cryptoCurrencyService.getExchanges(baseId)

    suspend fun getCandles(interval: String, baseId: String) =
        cryptoCurrencyService.getCandles(interval = interval, baseId = baseId)
}
