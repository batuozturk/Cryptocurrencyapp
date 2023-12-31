package com.batuhan.cryptocurrencyapp.presentation.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.core.SnackbarData
import com.batuhan.cryptocurrencyapp.core.views.CryptoCurrencyEditTextEventHandler
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.data.model.toConverterItem
import com.batuhan.cryptocurrencyapp.domain.GetConverterFavorites
import com.batuhan.cryptocurrencyapp.domain.GetPrices
import com.batuhan.cryptocurrencyapp.domain.GetPricesList
import com.batuhan.cryptocurrencyapp.domain.GetRate
import com.batuhan.cryptocurrencyapp.domain.GetRates
import com.batuhan.cryptocurrencyapp.domain.GetRatesFromDB
import com.batuhan.cryptocurrencyapp.domain.InsertPrices
import com.batuhan.cryptocurrencyapp.domain.InsertRate
import com.batuhan.cryptocurrencyapp.domain.InsertRates
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Integer.min
import java.text.DecimalFormatSymbols
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getPrices: GetPrices,
    private val getPricesList: GetPricesList,
    private val insertPrices: InsertPrices,
    private val getRates: GetRates,
    private val getRatesFromDB: GetRatesFromDB,
    private val insertRates: InsertRates,
    private val getConverterFavorites: GetConverterFavorites,
    private val getRate: GetRate,
    private val insertRate: InsertRate,
    savedStateHandle: SavedStateHandle
) : CryptoCurrencyViewModel(), NumberKeyboardListener, CryptoCurrencyEditTextEventHandler {

    private val _firstField = MutableLiveData<String>()
    val firstField: LiveData<String> = _firstField

    private val _firstCurrency = MutableLiveData<RateItem>()
    val firstCurrency: LiveData<RateItem> = _firstCurrency

    private val _secondCurrency = MutableLiveData<RateItem>()
    val secondCurrency: LiveData<RateItem> = _secondCurrency

    private val _thirdCurrency = MutableLiveData<RateItem>()
    val thirdCurrency: LiveData<RateItem> = _thirdCurrency

    private val _fourthCurrency = MutableLiveData<RateItem>()
    val fourthCurrency: LiveData<RateItem> = _fourthCurrency

    private var selectedCursorIndex: Int = 0

    private var priceJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val converterFavorites = firstField.asFlow().flatMapLatest {
        getConverterFavorites.invoke().filter { it.size == 4 || it.isEmpty() }.flatMapLatest {
            if (it.isEmpty()) {
                initValues()
                flowOf(listOf())
            } else {
                val first = it.get(0)
                val second =
                    calculatePrice(firstField.value ?: "0", first.rateUsd, it.get(1).rateUsd)
                val third =
                    calculatePrice(firstField.value ?: "0", first.rateUsd, it.get(2).rateUsd)
                val fourth =
                    calculatePrice(firstField.value ?: "0", first.rateUsd, it.get(3).rateUsd)

                _firstCurrency.postValue(first)
                _secondCurrency.postValue(it.get(1))
                _thirdCurrency.postValue(it.get(2))
                _fourthCurrency.postValue(it.get(3))
                flowOf(listOf(firstField.value ?: "0", second, third, fourth))
            }
        }
    }

    init {
        _firstField.value = "0"
    }

    fun setSelectedCursorIndex(index: Int) {
        selectedCursorIndex = index
    }

    fun initValues() {
        setCurrency(0, "bitcoin")
        setCurrency(1, "ethereum")
        setCurrency(2, "tether")
        setCurrency(3, "litecoin")
    }

    fun setCurrency(index: Int, currencyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getRate.invoke(GetRate.Params(currencyId))
            when (result) {
                is Result.Success -> {
                    insertRate.invoke(InsertRate.Params(result.data.copy(converterIndex = index)))
                }

                is Result.Error -> {
                }
            }
        }
    }

    fun initJob() {
        priceJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                getRatesListJob()
                getPricesListJob()
                delay(10000L)
            }
        }
    }

    fun clearJob() {
        priceJob?.cancel()
        priceJob = null
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
                }
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
                            initJob()
                        }
                    )
                )
            }
        }
    }

    suspend fun getPricesListJob() {
        val result = getPricesList.invoke()
        when (result) {
            is Result.Success -> {
                insertPrices.invoke(
                    InsertPrices.Params(
                        converterList = result.data.map {
                            it.copy(
                                changePercent24Hr = it.changePercent24Hr?.takeIf { it != "null" }
                                    ?: "0.0000000000",
                                priceUsd = it.priceUsd
                            ).toConverterItem()
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
                            initJob()
                        }
                    )
                )
            }
        }
    }

    override fun onLeftAuxButtonClicked() {
        val value = firstField.value ?: "0"
        if (value.contains(",") || value.contains(".")) return
        val updatedValue = StringBuilder().append(value)
            .insert(selectedCursorIndex, DecimalFormatSymbols.getInstance().decimalSeparator)
            .toString()

        _firstField.postValue(updatedValue)
    }

    override fun onNumberClicked(number: Int) {
        val value = firstField.value ?: ""
        var updatedValue =
            StringBuilder().append(value).insert(selectedCursorIndex, number).toString()
        if (updatedValue.getOrNull(0) == '0') updatedValue = updatedValue.substring(1)
        _firstField.postValue(updatedValue)
    }

    override fun onRightAuxButtonClicked() {
        val value = firstField.value
        if (value.isNullOrEmpty()) return
        else {
            var updatedValue = StringBuilder().append(value)
                .removeRange(
                    min(selectedCursorIndex + 0, value.length - 1),
                    min(selectedCursorIndex + 1, value.length)
                ).toString()
            if (updatedValue.indexOf(",") == 0) updatedValue = "0$updatedValue"
            else if (updatedValue.isEmpty()) updatedValue = "0"
            _firstField.postValue(updatedValue)
        }
    }

    override fun onCursorSelectionChanged(cursorPosition: Int) {
        selectedCursorIndex = cursorPosition
    }

    fun setCurrencyField(index: Int, currencyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getRate.invoke(GetRate.Params(currencyId))
            when (result) {
                is Result.Success -> {
                    updateNameAndPrice(index, result.data)
                }

                is Result.Error -> {
                }
            }
        }
    }

    fun updateNameAndPrice(index: Int, data: RateItem) {
        if (data.converterIndex != null && data.converterIndex != -1) return
        val rateItem = getRateItem(index)?.copy(converterIndex = -1) ?: return
        viewModelScope.launch(Dispatchers.IO) {
            insertRate.invoke(InsertRate.Params(rateItem))
            insertRate.invoke(InsertRate.Params(data.copy(converterIndex = index)))
        }
    }

    fun getRateItem(index: Int): RateItem? {
        return when (index) {
            0 -> firstCurrency.value
            1 -> secondCurrency.value
            2 -> thirdCurrency.value
            3 -> fourthCurrency.value
            else -> null
        }
    }

    fun calculatePrice(amount: String?, priceRate: String?, rate: String?): String {
        val newAmount = (amount ?: "0").toFloat()
        val priceRate = (priceRate ?: "1").toFloat()
        val newRate = (rate ?: "1").toFloat()
        return ((newAmount * priceRate) / (newRate)).toBigDecimal().toPlainString()
    }
}
