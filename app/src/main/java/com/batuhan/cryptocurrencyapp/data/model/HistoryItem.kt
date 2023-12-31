package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class HistoryItem(
    val priceUsd: String? = null,
    val time: Long? = null,
    @Json(ignore = true) val id: String? = null
)

@Keep
@JsonClass(generateAdapter = true)
data class HistoryItemResponse(
    val data: List<HistoryItem>?
)
