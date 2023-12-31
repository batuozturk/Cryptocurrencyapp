package com.batuhan.cryptocurrencyapp.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.core.Result
import com.batuhan.cryptocurrencyapp.core.SingleLiveEvent
import com.batuhan.cryptocurrencyapp.core.SnackbarData
import com.batuhan.cryptocurrencyapp.data.model.NewsSource
import com.batuhan.cryptocurrencyapp.data.model.RssItemData
import com.batuhan.cryptocurrencyapp.data.model.toRssItemData
import com.batuhan.cryptocurrencyapp.domain.GetRssChannel
import com.batuhan.cryptocurrencyapp.presentation.news.adapter.NewsEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getRssChannel: GetRssChannel,
    savedStateHandle: SavedStateHandle
) : CryptoCurrencyViewModel(), NewsEventHandler {

    private val _newsSource = MutableLiveData(NewsSource.COINTELEGRAPH)
    val newsSource: LiveData<NewsSource> = _newsSource

    private val _rssItems = MutableLiveData<List<RssItemData>>()
    val rssItems: LiveData<List<RssItemData>> = _rssItems

    private var selectedGuid: String? = null

    private val _selectedNewsUrl = SingleLiveEvent<String>()
    val selectedNewsUrl: LiveData<String> = _selectedNewsUrl

    init {
        getRssChannel(newsSource.value!!.url)
    }

    fun getRssChannel(url: String) {
        viewModelScope.launch {
            val result = getRssChannel.invoke(GetRssChannel.Params(url))
            when (result) {
                is Result.Success -> {
                    _rssItems.postValue(result.data.items.map { it.toRssItemData(selectedGuid) })
                }

                is Result.Error -> {
                    showSnackbar(
                        SnackbarData(
                            R.string.error_occurred,
                            actionTitle = R.string.retry,
                            backgroundColor = R.color.white,
                            textColor = R.color.black,
                            action = {
                                getRssChannel(newsSource.value!!.url)
                            }
                        )
                    )
                }
            }
        }
    }

    fun clearSelectedGuid() {
        selectedGuid = null
        clearRssList()
    }

    fun setSelectedGuid(guid: String) {
        selectedGuid = guid
        updateRssList(guid)
    }

    fun setSelectedNewsSource(string: String) {
        val source = NewsSource.valueOf(string)
        _newsSource.postValue(source)
        getRssChannel(url = source.url)
    }

    fun updateRssList(guid: String?) {
        val rssItems =
            rssItems.value?.map {
                if (it.rssItem.guid == guid) it.copy(isExpanded = it.rssItem.guid == guid)
                else it.copy(isExpanded = false)
            } ?: listOf()
        _rssItems.postValue(rssItems)
    }

    fun clearRssList() {
        val rssItems =
            rssItems.value?.map { it.copy(isExpanded = false) } ?: listOf()
        _rssItems.postValue(rssItems)
    }

    override fun onNewsExpanded(guid: String) {
        if (selectedGuid == guid) {
            clearSelectedGuid()
        } else {
            setSelectedGuid(guid)
        }
    }

    override fun onNewsClicked(url: String) {
        _selectedNewsUrl.postValue(url)
    }
}
