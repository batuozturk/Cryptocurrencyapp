package com.batuhan.cryptocurrencyapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
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
import com.batuhan.cryptocurrencyapp.data.source.local.CryptoCurrencyLocalDataSource
import com.batuhan.cryptocurrencyapp.data.source.remote.CryptoCurrencyDataSource
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptoCurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: CryptoCurrencyLocalDataSource,
    private val remoteDataSource: CryptoCurrencyDataSource,
    private val rssParser: RssParser
) : CryptoCurrencyRepository {

    override fun getPrices(filterType: FilterType, filterText: String): Flow<PagingData<CryptoCurrencyItem>> {
        return Pager(
            config = PagingConfig(
                10,
                enablePlaceholders = true,
                prefetchDistance = 3 * 10,
                initialLoadSize = 2 * 10
            ),
            pagingSourceFactory = {
                localDataSource.getPrices(filterType, filterText)
            }
        ).flow
    }

    override suspend fun getPricesList(): CryptoCurrencyItemResponse {
        return remoteDataSource.getPrices()
    }

    override suspend fun insertPrices(list: List<CryptoCurrencyResponseItem>) {
        return localDataSource.insertPrices(list)
    }

    override suspend fun insertPricesForConverter(list: List<CryptoCurrencyConverterResponseItem>) {
        return localDataSource.insertPricesForConverter(list)
    }

    override suspend fun getHistory(id: String, interval: String): HistoryItemResponse {
        return remoteDataSource.getHistory(id, interval)
    }

    override suspend fun getRssChannel(url: String): RssChannel {
        return rssParser.getRssChannel(url)
    }

    override suspend fun getRates(): RatesResponse {
        return remoteDataSource.getRates()
    }

    override suspend fun getRatesFromDB(): List<RateItem> {
        return localDataSource.getRates()
    }

    override suspend fun insertRates(list: List<RatesResponseItem>) {
        return localDataSource.insertRates(list)
    }

    override suspend fun insertPrice(item: CryptoCurrencyItem) {
        return localDataSource.insertPrice(item)
    }

    override suspend fun getPrice(id: String): CryptoCurrencyItem? {
        return localDataSource.getPrice(id)
    }

    override suspend fun insertRate(item: RateItem) {
        return localDataSource.insertRate(item)
    }

    override suspend fun getFavoriteRate(): RateItem? {
        return localDataSource.getFavoriteRate()
    }

    override fun getConverterFavorites(): Flow<List<RateItem>> {
        return localDataSource.getConverterFavorites()
    }

    override suspend fun getRate(id: String): RateItem {
        return localDataSource.getRate(id)
    }

    override fun getPriceFlow(id: String): Flow<CryptoCurrencyItem> {
        return localDataSource.getPriceFlow(id)
    }

    override suspend fun getExchanges(baseId: String): MarketItemResponse {
        return remoteDataSource.getExchanges(baseId)
    }

    override suspend fun getCandles(interval: String, baseId: String): CandleItemResponse {
        return remoteDataSource.getCandles(interval, baseId)
    }
}
