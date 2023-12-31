package com.batuhan.cryptocurrencyapp.presentation.detail.pages.exchanges

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.MarketItem
import com.batuhan.cryptocurrencyapp.domain.GetExchanges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Integer.min
import javax.inject.Inject

@HiltViewModel
class ExchangesViewModel @Inject constructor(private val getExchanges: GetExchanges) :
    CryptoCurrencyViewModel() {

    private val _exchangesList = MutableLiveData<List<MarketItem>?>()
    val exchangesList: LiveData<List<MarketItem>?> = _exchangesList

    var currencyId: String = ""

    private var job: Job? = null

    fun initCurrencyId(currencyId: String) {
        this.currencyId = currencyId
    }

    fun startRepeatingJob() {
        viewModelScope.launch {
            while (isActive) {
                job = initJob()
                job?.start()
                delay(10000L)
            }
        }
    }

    fun initJob(): Job {
        return viewModelScope.launch {
            val result = getExchanges.invoke(GetExchanges.Params(currencyId))
            when (result) {
                is Result.Success -> {
                    _exchangesList.postValue(
                        result.data.data?.map {
                            it.copy(
                                priceUsd = it.priceUsd?.substring(0, min(11, it.priceUsd.length)) ?: "0"
                            )
                        }
                    )
                }

                is Result.Error -> {
                    cancelJob()
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}
