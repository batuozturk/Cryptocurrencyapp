package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Keep
@Entity
data class RateItem(
    @PrimaryKey @ColumnInfo("id")
    val id: String = "",
    @ColumnInfo("symbol") val symbol: String?,
    @ColumnInfo("rateUsd") val rateUsd: String?,
    @ColumnInfo("isFavorite") val isFavorite: Boolean?,
    @ColumnInfo("converterIndex") val converterIndex: Int? = -1 // for indexing
)

@Keep
@JsonClass(generateAdapter = true)
data class RatesResponseItem(
    val id: String = "",
    val symbol: String?,
    val rateUsd: String?
)

@Keep
@JsonClass(generateAdapter = true)
data class RatesResponse(
    val data: List<RatesResponseItem>?
)
