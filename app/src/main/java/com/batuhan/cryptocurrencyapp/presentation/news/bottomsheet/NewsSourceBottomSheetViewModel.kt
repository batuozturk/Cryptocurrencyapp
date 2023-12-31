package com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.data.model.NewsSource
import com.batuhan.cryptocurrencyapp.data.model.NewsSourceData
import com.batuhan.cryptocurrencyapp.data.model.toNewsSourceData
import com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet.adapter.NewsSourceEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsSourceBottomSheetViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    CryptoCurrencyViewModel(), NewsSourceEventHandler {

    private val _newsSourceValues = MutableLiveData<List<NewsSourceData>>()
    val newsSourceValues: LiveData<List<NewsSourceData>> = _newsSourceValues

    private val _onDismiss = MutableLiveData<String>()
    val onDismiss: LiveData<String> = _onDismiss

    private var selectedNewsSource: String? = null

    override fun onSourceSelected(source: String) {
        _onDismiss.postValue(source)
    }

    fun setSelectedNewsSource(newsSource: String) {
        selectedNewsSource = newsSource
        _newsSourceValues.postValue(
            NewsSource.values().map { it.toNewsSourceData(selectedNewsSource) }
        )
    }
}
