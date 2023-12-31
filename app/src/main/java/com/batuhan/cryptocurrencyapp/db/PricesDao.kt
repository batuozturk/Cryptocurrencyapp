package com.batuhan.cryptocurrencyapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyConverterResponseItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PricesDao {

    @Upsert(entity = CryptoCurrencyItem::class)
    fun insertAll(prices: List<CryptoCurrencyResponseItem>)

    @Upsert(entity = CryptoCurrencyItem::class)
    fun insertAllForConverter(prices: List<CryptoCurrencyConverterResponseItem>)

    @Upsert
    fun insertItem(item: CryptoCurrencyItem)

    @RawQuery(observedEntities = [CryptoCurrencyItem::class])
    fun getPrices(query: SimpleSQLiteQuery): PagingSource<Int, CryptoCurrencyItem>

    @Query("SELECT * from cryptoCurrencyItem WHERE id =:id")
    fun getPrice(id: String): CryptoCurrencyItem?

    @Query("SELECT * from cryptocurrencyitem WHERE id =:id")
    fun getPriceFlow(id: String): Flow<CryptoCurrencyItem>
}
