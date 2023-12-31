package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.data.model.LocaleData
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage.adapter.SelectLanguageEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SelectLanguageViewModel @Inject constructor() :
    CryptoCurrencyViewModel(),
    SelectLanguageEventHandler {

    private val _languageList: MutableLiveData<List<LocaleData>?> = MutableLiveData()
    val languageList: LiveData<List<LocaleData>?> = _languageList

    private val _onDismiss = MutableLiveData<LocaleData>()
    val onDismiss: LiveData<LocaleData> = _onDismiss

    fun generateLocaleData(favoriteCode: String) {
        _languageList.postValue(
            mutableListOf<LocaleData>().apply {
                add(LocaleData(Locale("tr"), favoriteCode == "tr", R.string.turkish))
                add(
                    LocaleData(
                        Locale.ENGLISH,
                        favoriteCode == Locale.ENGLISH.language,
                        R.string.english
                    )
                )
                add(LocaleData(Locale.FRENCH, favoriteCode == Locale.FRENCH.language, R.string.french))
            }
        )
    }

    override fun onItemSelected(item: LocaleData) {
        _onDismiss.postValue(item)
    }
}
