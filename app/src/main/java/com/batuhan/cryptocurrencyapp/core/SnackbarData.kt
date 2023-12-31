package com.batuhan.cryptocurrencyapp.core

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class SnackbarData(
    @StringRes val title: Int,
    @StringRes val actionTitle: Int? = null,
    @ColorRes val backgroundColor: Int,
    @ColorRes val textColor: Int,
    inline val action: (() -> Unit)? = null
)
