package com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.SingleLiveEvent
import com.batuhan.cryptocurrencyapp.data.model.FilterType
import com.batuhan.cryptocurrencyapp.data.model.FilterTypeData
import com.batuhan.cryptocurrencyapp.data.model.toFilterTypeData
import com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet.adapter.PricesFilterEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PricesFilterViewModel @Inject constructor() :
    CryptoCurrencyViewModel(),
    PricesFilterEventHandler {

    private val _filters = MutableLiveData<List<FilterTypeData>>()
    val filters: LiveData<List<FilterTypeData>> = _filters

    private val _dismiss = SingleLiveEvent<String>()
    val dismiss: LiveData<String> = _dismiss

    fun initFilterTypes(selectedFilterName: String) {
        _filters.value = FilterType.values().map { it.toFilterTypeData(selectedFilterName) }
    }

    override fun onFilterSelected(name: String) {
        _dismiss.value = name
    }

}
