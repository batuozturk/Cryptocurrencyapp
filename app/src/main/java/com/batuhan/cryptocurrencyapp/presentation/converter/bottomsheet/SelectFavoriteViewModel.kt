package com.batuhan.cryptocurrencyapp.presentation.converter.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.domain.GetRatesFromDB
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency.adapter.SelectCurrencyEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectFavoriteViewModel @Inject constructor(
    private val getRatesFromDB: GetRatesFromDB
) :
    CryptoCurrencyViewModel(), SelectCurrencyEventHandler {

    private val _currencyList: MutableLiveData<List<RateItem>?> = MutableLiveData()
    val currencyList: LiveData<List<RateItem>?> = _currencyList

    private val _onDismiss = MutableLiveData<String>()
    val onDismiss: LiveData<String> = _onDismiss

    init {
        getRatesFromDB()
    }

    fun getRatesFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getRatesFromDB.invoke()) {
                is Result.Error -> {
                }

                is Result.Success -> {
                    _currencyList.postValue(result.data)
                }
            }
        }
    }

    override fun onItemSelected(item: RateItem) {
        _onDismiss.postValue(item.id)
    }
}
