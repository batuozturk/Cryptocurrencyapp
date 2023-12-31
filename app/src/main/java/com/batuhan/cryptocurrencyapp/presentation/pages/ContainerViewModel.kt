package com.batuhan.cryptocurrencyapp.presentation.pages

import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContainerViewModel @Inject constructor() : CryptoCurrencyViewModel()
