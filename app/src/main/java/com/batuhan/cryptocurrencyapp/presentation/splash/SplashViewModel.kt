package com.batuhan.cryptocurrencyapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor() : CryptoCurrencyViewModel() {

    private val _routing = SingleLiveEvent<Unit>()
    val routing: LiveData<Unit> = _routing

    init {
        routeToMainScreen()
    }

    fun routeToMainScreen() {
        viewModelScope.launch {
            delay(3000L)
            _routing.value = Unit
        }
    }
}
