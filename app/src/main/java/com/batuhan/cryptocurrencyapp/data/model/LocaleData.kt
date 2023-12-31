package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import java.util.Locale

@Keep
data class LocaleData(
    val locale: Locale,
    val isFavorite: Boolean,
    @StringRes val title: Int
)
