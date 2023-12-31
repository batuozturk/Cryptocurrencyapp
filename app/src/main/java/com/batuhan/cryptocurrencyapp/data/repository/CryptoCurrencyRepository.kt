package com.batuhan.cryptocurrencyapp.data.repository

import androidx.paging.PagingData
import com.batuhan.cryptocurrencyapp.data.model.CandleItemResponse
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyConverterResponseItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItemResponse
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyResponseItem
import com.batuhan.cryptocurrencyapp.data.model.FilterType
import com.batuhan.cryptocurrencyapp.data.model.HistoryItemResponse
import com.batuhan.cryptocurrencyapp.data.model.MarketItemResponse
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.RatesResponse
import com.batuhan.cryptocurrencyapp.data.model.RatesResponseItem
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.flow.Flow

interface CryptoCurrencyRepository {

    fun getPrices(filterType: FilterType, filterText: String): Flow<PagingData<CryptoCurrencyItem>>

    suspend fun getPricesList(): CryptoCurrencyItemResponse

    suspend fun insertPrices(list: List<CryptoCurrencyResponseItem>)

    suspend fun insertPricesForConverter(list: List<CryptoCurrencyConverterResponseItem>)

    suspend fun getHistory(id: String, interval: String): HistoryItemResponse

    suspend fun getRssChannel(url: String): RssChannel

    suspend fun getRates(): RatesResponse

    suspend fun getRatesFromDB(): List<RateItem>

    suspend fun insertRates(list: List<RatesResponseItem>)

    suspend fun insertPrice(item: CryptoCurrencyItem)

    suspend fun getPrice(id: String): CryptoCurrencyItem?

    suspend fun insertRate(item: RateItem)

    suspend fun getFavoriteRate(): RateItem?

    fun getConverterFavorites(): Flow<List<RateItem>>

    suspend fun getRate(id: String): RateItem

    fun getPriceFlow(id: String): Flow<CryptoCurrencyItem>

    suspend fun getExchanges(baseId: String): MarketItemResponse

    suspend fun getCandles(interval: String, baseId: String): CandleItemResponse
}
