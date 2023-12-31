package com.batuhan.cryptocurrencyapp.presentation.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.HistoryItem
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.toCryptoCurrencyItemData
import com.batuhan.cryptocurrencyapp.domain.GetFavoriteRate
import com.batuhan.cryptocurrencyapp.domain.GetPrice
import com.batuhan.cryptocurrencyapp.domain.GetPriceFlow
import com.batuhan.cryptocurrencyapp.domain.GetPriceHistory
import com.batuhan.cryptocurrencyapp.domain.GetPricesList
import com.batuhan.cryptocurrencyapp.domain.GetRates
import com.batuhan.cryptocurrencyapp.domain.InsertPrice
import com.batuhan.cryptocurrencyapp.domain.InsertPrices
import com.batuhan.cryptocurrencyapp.domain.InsertRates
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val getPriceFlow: GetPriceFlow,
    private val getPricesList: GetPricesList,
    private val getFavoriteRate: GetFavoriteRate,
    private val getRates: GetRates,
    private val insertRates: InsertRates,
    private val insertPrices: InsertPrices,
    private val getPriceHistory: GetPriceHistory,
    private val getPrice: GetPrice,
    private val insertPrice: InsertPrice
) : CryptoCurrencyViewModel(), OnTabSelectedListener {

    private var currencyId = MutableStateFlow("")

    var favoriteRate: RateItem? = null

    private val historyParameters = MutableStateFlow(Pair("", ""))

    private val _interval = MutableLiveData("m1")
    val interval: LiveData<String> = _interval

    private val _historyItems = MutableLiveData<List<HistoryItem>>()
    val historyItems: LiveData<List<HistoryItem>> = _historyItems

    @OptIn(ExperimentalCoroutinesApi::class)
    val price = currencyId.filter { it.isNotEmpty() }.flatMapLatest {
        getPriceFlow.invoke(GetPriceFlow.Params(it)).map {
            it.toCryptoCurrencyItemData(null, null)
        }
    }

    private var job: Job? = null
    private var historyJob: Job? = null

    fun initJob(currencyId: String): Job {
        this.currencyId.value = currencyId
        return viewModelScope.launch(Dispatchers.IO) {
            getRatesListJob()
            getPricesListJob()
        }
    }

    fun initHistoryJob(currencyId: String): Job {
        return viewModelScope.launch {
            getPriceHistoryJob(currencyId, interval.value!!)
        }
    }

    fun startRepeatingJob(currencyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                job = initJob(currencyId)
                job?.start()
                historyJob = initHistoryJob(currencyId)
                historyJob?.start()
                delay(5000L)
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        job = null
        historyJob?.cancel()
        historyJob = null
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
                // todo error
            }
        }
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
                cancelJob()
            }
        }
    }

    suspend fun getPriceHistoryJob(currencyId: String, interval: String) {
        val result = getPriceHistory.invoke(GetPriceHistory.Params(currencyId, interval))
        when (result) {
            is Result.Success -> {
                if (interval == this.interval.value!!) _historyItems.postValue(
                    result.data.data?.map {
                        it.copy(
                            priceUsd = favoriteRate?.let { rate ->
                                calculatePrice(
                                    it.priceUsd,
                                    rate.rateUsd
                                )
                            } ?: it.priceUsd
                        )
                    } ?: listOf()
                )
            }

            is Result.Error -> {
                _historyItems.postValue(listOf())
            }
        }
    }

    fun calculatePrice(price: String?, rate: String?): String {
        val newPrice = (price ?: "0").toFloat()
        val newRate = (rate ?: "1").toFloat()
        return (newPrice / newRate).toString()
    }

    fun getTabValue(tab: TabLayout.Tab?): String? {
        return when (tab?.position) {
            0 -> "m1"
            1 -> "m5"
            2 -> "m15"
            3 -> "m30"
            4 -> "h1"
            5 -> "h6"
            6 -> "h12"
            7 -> "d1"
            else -> null
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewModelScope.launch {
            _interval.postValue(getTabValue(tab))
            delay(2000L)
            historyJob?.cancel()
            historyJob = initHistoryJob(currencyId.value)
            historyJob?.start()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // no-op
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // no-op
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
}
