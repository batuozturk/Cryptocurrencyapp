package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.batuhan.cryptocurrencyapp.R

@Keep
enum class FilterType(@StringRes val title: Int, val query: String) {
    FAVORITES(R.string.favorites, "WHERE isFavorite = true"),
    RANK(R.string.rank, "ORDER BY CAST(rank AS INT) ASC"),
    GAINERS(
        R.string.most_gained,
        "ORDER BY CAST(changePercent24Hr AS FLOAT) DESC"
    ),
    LOSERS(
        R.string.losers,
        "ORDER BY CAST(changePercent24Hr AS FLOAT) ASC"
    ),
    MARKET_CAP(
        R.string.market_cap,
        "ORDER BY CAST(marketCapUsd AS INT) DESC"
    ),
    A_Z(R.string.alphabetical, "ORDER BY name ASC"),
    Z_A(R.string.alphabetical_reverse, "ORDER BY name DESC"),
}

fun FilterType.createFilteredQuery(filterText: String): String {
    return getPrefix() + getFilteredTextPart(filterText) + getPostfix(filterText)
}

fun FilterType.getPostfix(filterText: String): String {
    return takeIf { it == FilterType.FAVORITES && filterText.isNotEmpty() && filterText.isNotBlank() }
        ?.let { "AND isFavorite = true" } ?: query
}

fun FilterType.getPrefix() = "SELECT * FROM cryptocurrencyitem "

fun FilterType.getFilteredTextPart(filterText: String): String {
    return filterText.takeIf { it.isNotBlank() && it.isNotEmpty() }
        ?.let { "WHERE name LIKE ? " } ?: ""
}

fun FilterType.toFilterTypeData(selectedFilterType: String): FilterTypeData {
    return FilterTypeData(this, this.name == selectedFilterType)
}
