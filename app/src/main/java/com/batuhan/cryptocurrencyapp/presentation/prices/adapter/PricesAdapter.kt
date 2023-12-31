package com.batuhan.cryptocurrencyapp.presentation.prices.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItemData
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyState
import com.batuhan.cryptocurrencyapp.data.model.HistoryItem
import com.batuhan.cryptocurrencyapp.databinding.CryptoCurrencyItemViewBinding
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.axisBuilder
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.scroll.ChartScrollSpec
import java.lang.Integer.max
import java.lang.Integer.min
import java.text.DecimalFormat

class PricesAdapter() :
    PagingDataAdapter<CryptoCurrencyItemData, PricesAdapter.PricesViewHolder>(object :
            DiffUtil.ItemCallback<CryptoCurrencyItemData>() {
            override fun areItemsTheSame(
                oldItem: CryptoCurrencyItemData,
                newItem: CryptoCurrencyItemData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CryptoCurrencyItemData,
                newItem: CryptoCurrencyItemData
            ): Boolean {
                return oldItem.price == newItem.price &&
                    oldItem.isExpanded == newItem.isExpanded &&
                    oldItem.change == newItem.change &&
                    oldItem.historyItems == newItem.historyItems &&
                    oldItem.isFavorite == newItem.isFavorite
            }
        }) {

    var eventHandler: PricesEventHandler? = null

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricesViewHolder {
        val binding = CryptoCurrencyItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PricesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PricesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PricesViewHolder(private val binding: CryptoCurrencyItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CryptoCurrencyItemData) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onDetailClicked(item.id ?: return@setOnClickListener)
                }
                val visibility = if (item.isExpanded) {
                    View.VISIBLE
                } else View.GONE
                graphCard.visibility = visibility
                val drawable = when (item.state) {
                    CryptoCurrencyState.PLUS -> R.drawable.ic_circle_plus_24
                    CryptoCurrencyState.MINUS -> R.drawable.ic_circle_minus_24
                    CryptoCurrencyState.UNCH -> R.drawable.ic_circle_unch_24
                }

                state.setImageResource(drawable)
                price.text = item.price?.toBigDecimal()?.toPlainString()
                    ?.substring(0, min(11, item.price.length))
                change.text = item.change?.toBigDecimal()?.toPlainString()
                    ?.substring(0, max(item.change.length - 9, 5)) + "%"
                name.text = item.name
                val expandDrawable =
                    if (item.isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
                expandItem.setImageResource(expandDrawable)
                expandItem.setOnClickListener {
                    eventHandler?.onExpanded(item.id ?: return@setOnClickListener)
                }
                val favoriteDrawable =
                    if (item.isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
                favoriteItem.setImageResource(favoriteDrawable)
                favoriteItem.setOnClickListener {
                    eventHandler?.onFavorite(item.id ?: return@setOnClickListener)
                }
                if (item.historyItems.isNullOrEmpty() || item.historyItems[0].id != item.id) {
                    progressIndicator.visibility = View.VISIBLE
                    chartView.visibility = View.INVISIBLE
                } else {
                    progressIndicator.visibility = View.GONE
                    val items = (
                        item.historyItems.filter { it.priceUsd != null }
                            .map { it.priceUsd!!.toFloat() }
                        ).toTypedArray()
                    val chartEntryModel = entryModelOf(*items)
                    (chartView.chart as LineChart).apply {
                        axisBuilder<AxisPosition.Vertical.Start> {
                            axisValuesOverrider =
                                object : AxisValuesOverrider<ChartEntryModel> {
                                    override fun getMinY(model: ChartEntryModel) = model.minY
                                    override fun getMaxY(model: ChartEntryModel) = model.maxY
                                }
                            valueFormatter =
                                DecimalFormatAxisValueFormatter(DecimalFormat("0.000000"))
                        }
                        lines[0].apply {
                            lineColor = root.context.getColor(getColor(item.state))
                        }
                    }
                    chartView.chartScrollSpec = ChartScrollSpec(isScrollEnabled = false)
                    chartView.setModel(chartEntryModel)
                    chartView.visibility = View.VISIBLE
                }
            }
        }
    }
}

fun HistoryItem.toFloatEntry(): FloatEntry {
    return FloatEntry(
        time!!.toFloat(),
        priceUsd!!.toFloat()
    )
}

@ColorRes
fun getColor(state: CryptoCurrencyState): Int {
    return when (state) {
        CryptoCurrencyState.PLUS -> R.color.green
        CryptoCurrencyState.MINUS -> R.color.red
        CryptoCurrencyState.UNCH -> R.color.black
    }
}

interface PricesEventHandler {

    fun onExpanded(id: String)

    fun onDetailClicked(id: String)

    fun onFavorite(id: String)
}
