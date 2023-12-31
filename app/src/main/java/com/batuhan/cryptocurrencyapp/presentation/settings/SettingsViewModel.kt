package com.batuhan.cryptocurrencyapp.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.SingleLiveEvent
import com.batuhan.cryptocurrencyapp.data.model.SettingsItem
import com.batuhan.cryptocurrencyapp.data.model.SettingsItemRouting
import com.batuhan.cryptocurrencyapp.data.model.generateSettingsList
import com.batuhan.cryptocurrencyapp.domain.GetRatesFromDB
import com.batuhan.cryptocurrencyapp.presentation.settings.adapter.SettingsEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : CryptoCurrencyViewModel(), SettingsEventHandler {

    private val _settingsList = MutableLiveData(generateSettingsList())
    val settingsList: LiveData<List<SettingsItem>> = _settingsList

    private val _routing = SingleLiveEvent<SettingsItemRouting>()
    val routing: LiveData<SettingsItemRouting> = _routing

    override fun onItemClick(item: SettingsItem) {
        _routing.postValue(item.routing)
    }
}
