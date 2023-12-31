package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class CryptoCurrencyResponseItem(
    val changePercent24Hr: String?,
    @PrimaryKey @ColumnInfo("id")
    val id: String = "",
    val marketCapUsd: String?,
    val maxSupply: String?,
    val name: String?,
    val priceUsd: String?,
    val rank: String?,
    val supply: String?,
    val symbol: String?,
    val volumeUsd24Hr: String?,
    val vwap24Hr: String?,
    val price: String?
)

@Keep
@JsonClass(generateAdapter = true)
data class CryptoCurrencyConverterResponseItem(
    val changePercent24Hr: String?,
    @PrimaryKey @ColumnInfo("id")
    val id: String = "",
    val marketCapUsd: String?,
    val maxSupply: String?,
    val name: String?,
    val priceUsd: String?,
    val rank: String?,
    val supply: String?,
    val symbol: String?,
    val volumeUsd24Hr: String?,
    val vwap24Hr: String?
)

fun CryptoCurrencyResponseItem.toConverterItem(): CryptoCurrencyConverterResponseItem {
    return CryptoCurrencyConverterResponseItem(
        changePercent24Hr,
        id,
        marketCapUsd,
        maxSupply,
        name,
        priceUsd,
        rank,
        supply,
        symbol,
        volumeUsd24Hr,
        vwap24Hr
    )
}

@Keep
@Entity
data class CryptoCurrencyItem(
    @ColumnInfo("changePercent24Hr") val changePercent24Hr: String?,
    @PrimaryKey @ColumnInfo("id")
    val id: String = "",
    @ColumnInfo("marketCapUsd") val marketCapUsd: String?,
    @ColumnInfo("maxSupply") val maxSupply: String?,
    @ColumnInfo("name") val name: String?,
    @ColumnInfo("priceUsd") val priceUsd: String?,
    @ColumnInfo("price") val price: String?,
    @ColumnInfo("rank") val rank: String?,
    @ColumnInfo("supply") val supply: String?,
    @ColumnInfo("symbol") val symbol: String?,
    @ColumnInfo("volumeUsd24Hr") val volumeUsd24Hr: String?,
    @ColumnInfo("vwap24Hr") val vwap24Hr: String?,
    @ColumnInfo("isFavorite") val isFavorite: Boolean?
)

@Keep
@JsonClass(generateAdapter = true)
data class CryptoCurrencyItemResponse(
    val data: List<CryptoCurrencyResponseItem>?
)

fun CryptoCurrencyItem.toCryptoCurrencyItemData(
    expandedId: String?,
    historyItems: List<HistoryItem>?
): CryptoCurrencyItemData {
    return CryptoCurrencyItemData(
        id = id,
        name = name,
        price = price,
        rank = rank,
        change = changePercent24Hr,
        state = if (changePercent24Hr?.contains('-') == true) CryptoCurrencyState.MINUS
        else if (changePercent24Hr?.toFloat() == 0f) CryptoCurrencyState.UNCH
        else if ((changePercent24Hr?.toFloat() ?: 0f) > 0f) CryptoCurrencyState.PLUS
        else CryptoCurrencyState.UNCH,
        isExpanded = id == expandedId,
        historyItems = historyItems,
        isFavorite = isFavorite ?: false
    )
}
