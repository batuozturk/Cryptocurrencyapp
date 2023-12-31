package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
data class DetailInfoData(
    @StringRes val title: Int,
    val value: String
)
