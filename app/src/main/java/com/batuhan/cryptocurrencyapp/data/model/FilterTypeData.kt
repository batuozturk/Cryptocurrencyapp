package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep

@Keep
data class FilterTypeData(
    val filterType: FilterType,
    val isSelected: Boolean = false
)
