package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep

@Keep
data class CryptoCurrencyItemData(
    val id: String? = null,
    val name: String? = null,
    val rank: String? = null,
    val price: String? = null,
    val change: String? = null,
    val state: CryptoCurrencyState = CryptoCurrencyState.UNCH,
    val isExpanded: Boolean = false,
    val historyItems: List<HistoryItem>? = null,
    val isFavorite: Boolean = false
)

@Keep
enum class CryptoCurrencyState {
    PLUS, MINUS, UNCH
}
