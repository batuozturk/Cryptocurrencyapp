package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import com.prof18.rssparser.model.RssItem

@Keep
data class RssItemData(
    val rssItem: RssItem,
    val isExpanded: Boolean = false
)

fun RssItem.toRssItemData(guid: String?): RssItemData {
    return RssItemData(
        rssItem = this,
        isExpanded = guid == this.guid
    )
}
