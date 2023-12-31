package com.batuhan.cryptocurrencyapp.data.source.local

import androidx.paging.PagingSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyConverterResponseItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyResponseItem
import com.batuhan.cryptocurrencyapp.data.model.FilterType
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.RatesResponseItem
import com.batuhan.cryptocurrencyapp.data.model.createFilteredQuery
import com.batuhan.cryptocurrencyapp.db.AppDatabase
import javax.inject.Inject

class CryptoCurrencyLocalDataSource @Inject constructor(private val database: AppDatabase) {

    fun getPrices(
        filterType: FilterType,
        filterText: String
    ): PagingSource<Int, CryptoCurrencyItem> {
        return database.pricesDao().getPrices(
            SimpleSQLiteQuery(
                filterType.createFilteredQuery(filterText),
                filterText.takeIf { it.isNotBlank() && it.isNotEmpty() }
                    ?.let { arrayOf(it + "%") }
            )
        )
    }

    suspend fun insertPrices(list: List<CryptoCurrencyResponseItem>) {
        return database.pricesDao().insertAll(list)
    }

    suspend fun insertPricesForConverter(list: List<CryptoCurrencyConverterResponseItem>) {
        return database.pricesDao().insertAllForConverter(list)
    }

    suspend fun getRates(): List<RateItem> {
        return database.ratesDao().getRates()
    }

    suspend fun insertRates(list: List<RatesResponseItem>) {
        return database.ratesDao().insertRates(list)
    }

    suspend fun insertPrice(item: CryptoCurrencyItem) {
        return database.pricesDao().insertItem(item)
    }

    suspend fun getPrice(id: String): CryptoCurrencyItem? {
        return database.pricesDao().getPrice(id)
    }

    suspend fun insertRate(item: RateItem) {
        return database.ratesDao().insertRate(item)
    }

    suspend fun getFavoriteRate() = database.ratesDao().getFavoriteRate()

    fun getConverterFavorites() = database.ratesDao().getConverterFavorites()

    suspend fun getRate(id: String): RateItem {
        return database.ratesDao().getRate(id)
    }

    fun getPriceFlow(id: String) = database.pricesDao().getPriceFlow(id)
}
