package com.batuhan.cryptocurrencyapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.core.SnackbarData
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyState
import com.batuhan.cryptocurrencyapp.data.model.HistoryItem
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.toCryptoCurrencyItemData
import com.batuhan.cryptocurrencyapp.domain.GetFavoriteRate
import com.batuhan.cryptocurrencyapp.domain.GetPriceHistory
import com.batuhan.cryptocurrencyapp.domain.GetPrice
import com.batuhan.cryptocurrencyapp.domain.GetPriceFlow
import com.batuhan.cryptocurrencyapp.domain.GetPricesList
import com.batuhan.cryptocurrencyapp.domain.GetRates
import com.batuhan.cryptocurrencyapp.domain.InsertPrice
import com.batuhan.cryptocurrencyapp.domain.InsertPrices
import com.batuhan.cryptocurrencyapp.domain.InsertRates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailViewModel @Inject constructor(
    private val getPriceFlow: GetPriceFlow,
    private val getPriceHistory: GetPriceHistory,
    private val getPricesList: GetPricesList,
    private val insertPrices: InsertPrices,
    private val getRates: GetRates,
    private val insertRates: InsertRates,
    private val insertPrice: InsertPrice,
    private val getFavoriteRate: GetFavoriteRate,
    private val getPrice: GetPrice,
    savedStateHandle: SavedStateHandle
) : CryptoCurrencyViewModel() {

    var currencyId = savedStateHandle.get<String>("currencyId") ?: "bitcoin"

    private var job: Job? = null

    private val _historyItems = MutableLiveData<List<HistoryItem>?>()
    val historyItems: LiveData<List<HistoryItem>?> = _historyItems

    var scrollToTop = false

    var state: CryptoCurrencyState? = null

    var favoriteRate: RateItem? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val price = getPriceFlow.invoke(GetPriceFlow.Params(currencyId)).flatMapLatest {
        flowOf(it.toCryptoCurrencyItemData(null, null))
    }

    fun startRepeatingJob(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                getRatesListJob()
                getPricesListJob()
                getHistoryJob(currencyId)

                delay(10000L)
            }
        }
    }

    fun initializeJob() {
        job = startRepeatingJob()
    }

    fun clearJob() {
        job?.cancel()
        job = null
    }

    suspend fun getRatesListJob() {
        val result = getRates.invoke()
        when (result) {
            is Result.Success -> {
                result.data.data?.let {
                    insertRates.invoke(
                        InsertRates.Params(
                            it
                        )
                    )
                    val result1 = getFavoriteRate.invoke()
                    when (result1) {
                        is Result.Success -> {
                            favoriteRate = result1.data
                        }

                        is Result.Error -> {
                            // no-op
                        }
                    }
                }
            }

            is Result.Error -> {
                clearJob()
            }
        }
    }

    suspend fun getPricesListJob() {
        val result = getPricesList.invoke()
        when (result) {
            is Result.Success -> {

                insertPrices.invoke(
                    InsertPrices.Params(
                        list = result.data.map {
                            it.copy(
                                changePercent24Hr = it.changePercent24Hr?.takeIf { it != "null" }
                                    ?: "0.0000000000",
                                priceUsd = it.priceUsd,
                                price = favoriteRate?.let { rate ->
                                    calculatePrice(
                                        it.priceUsd,
                                        rate.rateUsd
                                    )
                                } ?: it.priceUsd
                            )
                        }
                    )
                )
            }

            is Result.Error -> {
                clearJob()
                showSnackbar(
                    SnackbarData(
                        R.string.error_occurred,
                        actionTitle = R.string.retry,
                        backgroundColor = R.color.white,
                        textColor = R.color.black,
                        action = {
                            initializeJob()
                        }
                    )
                )
            }
        }
    }

    suspend fun getHistoryJob(expandedId: String) {
        val result = getPriceHistory.invoke(GetPriceHistory.Params(expandedId, "m1"))
        when (result) {
            is Result.Success -> {
                _historyItems.postValue(
                    result.data.data?.map {
                        it.copy(
                            id = expandedId,
                            priceUsd = favoriteRate?.let { rate ->
                                calculatePrice(
                                    it.priceUsd,
                                    rate.rateUsd
                                )
                            } ?: it.priceUsd
                        )
                    }
                )
            }

            is Result.Error -> {
                clearJob()
                showSnackbar(
                    SnackbarData(
                        R.string.error_occurred,
                        actionTitle = R.string.retry,
                        backgroundColor = R.color.white,
                        textColor = R.color.black,
                        action = {
                            initializeJob()
                        }
                    )
                )
            }
        }
    }

    fun onFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getPrice.invoke(GetPrice.Params(id))) {
                is Result.Success -> {
                    result.data?.let {
                        insertPrice.invoke(
                            InsertPrice.Params(
                                it.copy(isFavorite = !(it.isFavorite ?: false))
                            )
                        )
                    }
                }

                is Result.Error -> {
                    // no-op
                }
            }
        }
    }

    fun calculatePrice(price: String?, rate: String?): String {
        val newPrice = (price ?: "0").toFloat()
        val newRate = (rate ?: "1").toFloat()
        return (newPrice / newRate).toString()
    }
}
