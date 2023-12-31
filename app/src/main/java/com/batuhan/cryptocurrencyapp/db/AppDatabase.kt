package com.batuhan.cryptocurrencyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.RateItem

@Database(
    version = 1,
    entities = [CryptoCurrencyItem::class, RateItem::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pricesDao(): PricesDao

    abstract fun ratesDao(): RatesDao
}
