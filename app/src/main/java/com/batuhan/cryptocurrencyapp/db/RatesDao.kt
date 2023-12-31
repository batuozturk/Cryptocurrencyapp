package com.batuhan.cryptocurrencyapp.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.RatesResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {

    @Upsert(RateItem::class)
    fun insertRates(list: List<RatesResponseItem>)

    @Query("SELECT * from rateitem")
    fun getRates(): List<RateItem>

    @Query("SELECT * from rateitem WHERE isFavorite = 1")
    fun getFavoriteRate(): RateItem?

    @Upsert
    fun insertRate(item: RateItem)

    @Query("SELECT * from rateitem WHERE id =:id")
    fun getRate(id: String): RateItem

    @Query("SELECT * from rateitem WHERE converterIndex NOT NULL AND converterIndex != -1 ORDER BY converterIndex ASC")
    fun getConverterFavorites(): Flow<List<RateItem>>
}
