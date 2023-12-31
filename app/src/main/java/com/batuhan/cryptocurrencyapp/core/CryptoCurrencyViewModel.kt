package com.batuhan.cryptocurrencyapp.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class CryptoCurrencyViewModel : ViewModel() {

    private val _showSnackbar = SingleLiveEvent<SnackbarData>()
    val showSnackbar: LiveData<SnackbarData> = _showSnackbar

    fun showSnackbar(snackbarData: SnackbarData) {
        _showSnackbar.postValue(snackbarData)
    }
}
