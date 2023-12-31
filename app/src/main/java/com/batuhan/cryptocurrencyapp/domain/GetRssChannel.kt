package com.batuhan.cryptocurrencyapp.domain

import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.repository.CryptoCurrencyRepository
import com.prof18.rssparser.model.RssChannel
import javax.inject.Inject

class GetRssChannel @Inject constructor(private val repository: CryptoCurrencyRepository) {

    data class Params(val url: String)

    suspend operator fun invoke(params: Params): Result<RssChannel> {
        return runCatching {
            Result.Success(repository.getRssChannel(params.url))
        }.getOrElse {
            Result.Error(it)
        }
    }
}
