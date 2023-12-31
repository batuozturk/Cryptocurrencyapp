package com.batuhan.cryptocurrencyapp.presentation.detail.pages.detailinfo

import androidx.lifecycle.SavedStateHandle
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyViewModel
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItem
import com.batuhan.cryptocurrencyapp.data.model.DetailInfoData
import com.batuhan.cryptocurrencyapp.domain.GetPriceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.lang.Integer.min
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val getPriceFlow: GetPriceFlow,
    savedStateHandle: SavedStateHandle
) :
    CryptoCurrencyViewModel() {

    lateinit var priceDetailFlow: Flow<List<DetailInfoData>>

    fun initCurrencyId(currencyId: String) {
        priceDetailFlow = getPriceFlow.invoke(GetPriceFlow.Params(currencyId)).flatMapLatest {
            flowOf(it.toDetailItemDataList())
        }
    }

    fun CryptoCurrencyItem.toDetailItemDataList(): List<DetailInfoData> {
        val list = mutableListOf<DetailInfoData>()
        list.add(
            DetailInfoData(
                R.string.market_cap,
                this.marketCapUsd?.substring(
                    0,
                    this.marketCapUsd.indexOf(".").takeIf { it != -1 } ?: min(
                        20,
                        this.marketCapUsd.length
                    )
                ) ?: "undefined"
            )
        )
        list.add(
            DetailInfoData(
                R.string.max_supply,
                this.maxSupply?.substring(
                    0,
                    this.maxSupply.indexOf(".").takeIf { it != -1 } ?: min(
                        20,
                        this.maxSupply.length
                    )
                ) ?: "undefined"
            )
        )
        list.add(
            DetailInfoData(
                R.string.volume_24_hr,
                this.volumeUsd24Hr?.toBigDecimal()?.toPlainString()
                    ?.substring(
                        0,
                        this.volumeUsd24Hr.indexOf(".").takeIf { it != -1 } ?: min(
                            20,
                            this.volumeUsd24Hr.length
                        )
                    ) ?: "undefined"
            )
        )
        list.add(
            DetailInfoData(
                R.string.supply,
                this.supply?.toBigDecimal()?.toPlainString()
                    ?.substring(
                        0,
                        this.supply.indexOf(".").takeIf { it != -1 } ?: min(20, this.supply.length)
                    )
                    ?: "undefined"
            )
        )
        list.add(
            DetailInfoData(
                R.string.vwap_24_hr,
                this.vwap24Hr?.substring(0, min(11, this.vwap24Hr.length)) ?: "undefined"
            )
        )
        return list
    }
}
