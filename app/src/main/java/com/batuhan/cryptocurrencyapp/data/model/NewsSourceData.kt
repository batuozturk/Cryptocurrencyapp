package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep

@Keep
data class NewsSourceData(
    val newsSource: NewsSource,
    val isSelected: Boolean = false
)

fun NewsSource.toNewsSourceData(selectedName: String?): NewsSourceData {
    return NewsSourceData(this, this.name == selectedName)
}
