package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class MarketItem(
    val exchangeId: String?,
    val priceUsd: String?
)

@Keep
@JsonClass(generateAdapter = true)
data class MarketItemResponse(
    val data: List<MarketItem>?
)
