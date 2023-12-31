package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class CandleItemResponse(
    val data: List<CandleItem>?
)

@Keep
@JsonClass(generateAdapter = true)
data class CandleItem(
    val open: String?,
    val high: String?,
    val low: String?,
    val close: String?,
    val volume: String?,
    val period: Long?
)
