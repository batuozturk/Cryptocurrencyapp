package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.domain.GetFavoriteRate
import com.batuhan.cryptocurrencyapp.domain.GetRatesFromDB
import com.batuhan.cryptocurrencyapp.domain.InsertRate
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency.adapter.SelectCurrencyEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCurrencyViewModel @Inject constructor(
    private val getRatesFromDB: GetRatesFromDB,
    private val insertRate: InsertRate,
    private val getFavoriteRate: GetFavoriteRate
) :
    CryptoCurrencyViewModel(), SelectCurrencyEventHandler {

    private val _currencyList: MutableLiveData<List<RateItem>?> = MutableLiveData()
    val currencyList: LiveData<List<RateItem>?> = _currencyList

    private val _onDismiss = MutableLiveData<Unit>()
    val onDismiss: LiveData<Unit> = _onDismiss

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
        viewModelScope.launch(Dispatchers.IO) {
            val result1 = getFavoriteRate.invoke()
            when (result1) {
                is Result.Success -> {
                    result1.data?.let {
                        insertRate.invoke(InsertRate.Params(it.copy(isFavorite = false)))
                    }
                }

                is Result.Error -> {
                }
            }
            val result2 = insertRate.invoke(InsertRate.Params(item.copy(isFavorite = true)))
            when (result2) {
                is Result.Success -> {
                    _onDismiss.postValue(Unit)
                }

                is Result.Error -> {
                }
            }
        }
    }
}
