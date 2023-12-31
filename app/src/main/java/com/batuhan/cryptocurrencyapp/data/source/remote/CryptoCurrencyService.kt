package com.batuhan.cryptocurrencyapp.data.source.remote

import com.batuhan.cryptocurrencyapp.data.model.CandleItemResponse
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItemResponse
import com.batuhan.cryptocurrencyapp.data.model.HistoryItemResponse
import com.batuhan.cryptocurrencyapp.data.model.MarketItemResponse
import com.batuhan.cryptocurrencyapp.data.model.RatesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoCurrencyService {

    companion object {
        private const val PATH_VERSION = "v2"
    }

    @GET("$PATH_VERSION/assets")
    suspend fun getPrices(@Query("limit") limit: String = "2000"): CryptoCurrencyItemResponse

    @GET("$PATH_VERSION/assets/{id}/history")
    suspend fun getHistory(
        @Path("id") id: String,
        @Query("interval") interval: String
    ): HistoryItemResponse

    @GET("$PATH_VERSION/rates")
    suspend fun getRates(): RatesResponse

    @GET("$PATH_VERSION/markets")
    suspend fun getExchanges(
        @Query("baseId") baseId: String,
        @Query("quoteId") quoteId: String = "united-states-dollar"
    ): MarketItemResponse

    @GET("$PATH_VERSION/candles")
    suspend fun getCandles(
        @Query("exchange") exchange: String = "binance",
        @Query("interval") interval: String,
        @Query("baseId") baseId: String,
        @Query("quoteId") quoteId: String = "united-states-dollar"
    ): CandleItemResponse
}
