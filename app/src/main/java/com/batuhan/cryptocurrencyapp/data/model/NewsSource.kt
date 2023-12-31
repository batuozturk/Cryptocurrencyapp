package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.batuhan.cryptocurrencyapp.R

@Keep
enum class NewsSource(@StringRes val title: Int, val url: String) {

    COINTELEGRAPH(R.string.cointelegraph, "https://cointelegraph.com/rss"),
    COINZENE(R.string.coinzene, "https://coinzene.com/feed/"),
    CRYPTOPOTATO(R.string.cryptopotato, "https://cryptopotato.com/feed/"),
    COINDESK(R.string.coindesk, "https://www.coindesk.com/arc/outboundfeeds/rss/")
}
