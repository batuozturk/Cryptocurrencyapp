package com.batuhan.cryptocurrencyapp.presentation.graph

import android.content.Context
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItemData
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyState
import com.batuhan.cryptocurrencyapp.data.model.HistoryItem
import com.batuhan.cryptocurrencyapp.databinding.ActivityGraphBinding
import com.google.android.material.tabs.TabLayout
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.axisBuilder
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.scroll.ChartScrollSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Locale

@AndroidEntryPoint
class GraphActivity : AppCompatActivity() {

    private val viewModel: GraphViewModel by viewModels()

    private var binding: ActivityGraphBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding = ActivityGraphBinding.inflate(LayoutInflater.from(this))
        binding!!.graphTabLayout.addOnTabSelectedListener(viewModel)
        setContentView(binding!!.root)
        viewModel.run {
            historyItems.observe(this@GraphActivity) {
                if (it.isNotEmpty()) updateGraphCard(it)
            }
            interval.observe(this@GraphActivity) {
                binding!!.graphChartView.visibility = View.GONE
                binding!!.graphProgressIndicator.visibility = View.VISIBLE
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    price.collectLatest {
                        updatePriceCard(it)
                        (binding!!.graphChartView.chart as LineChart).apply {
                            lines[0].apply {
                                lineColor = this@GraphActivity.getColor(
                                    com.batuhan.cryptocurrencyapp.presentation.prices.adapter.getColor(
                                        it.state
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val base: Context
        val config = newBase.resources.configuration
        val langCode =
            newBase.getSharedPreferences("lang_pref", MODE_PRIVATE).getString("lang", Locale.getDefault().language)
        val locales = LocaleList.forLanguageTags(langCode)
        config.setLocales(locales)
        base = newBase.createConfigurationContext(config)
        super.attachBaseContext(base)
    }

    override fun onResume() {
        super.onResume()
        intent.extras?.getString("currencyId")?.let {
            viewModel.startRepeatingJob(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelJob()
    }

    fun updatePriceCard(item: CryptoCurrencyItemData) {
        binding!!.graphName.text = item.name
        binding!!.graphChange.text =
            item.change?.toBigDecimal()?.toPlainString()?.substring(
            0,
            Integer.max(item.change.length - 9, 5)
        ) + "%"
        binding!!.graphPrice.text = item.price
        val drawable = when (item.state) {
            CryptoCurrencyState.PLUS -> R.drawable.ic_circle_plus_24
            CryptoCurrencyState.MINUS -> R.drawable.ic_circle_minus_24
            CryptoCurrencyState.UNCH -> R.drawable.ic_circle_unch_24
        }
        binding!!.graphState.setImageResource(drawable)
        val favoriteDrawable =
            if (item.isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
        binding!!.graphFavoriteItem.setImageResource(favoriteDrawable)
        binding!!.graphFavoriteItem.setOnClickListener {
            viewModel.onFavorite(item.id ?: return@setOnClickListener)
        }
    }

    fun updateGraphCard(list: List<HistoryItem>) {
        binding?.run {
            val items = (
                list.filter { it.priceUsd != null }
                    .map { it.priceUsd!!.toFloat() }
                ).toTypedArray()
            val chartEntryModel = entryModelOf(*items)
            (graphChartView.chart as? LineChart)?.apply {
                axisValuesOverrider =
                    object : AxisValuesOverrider<ChartEntryModel> {
                        override fun getMinY(model: ChartEntryModel) = model.minY
                        override fun getMaxY(model: ChartEntryModel) = model.maxY
                    }
                axisBuilder<AxisPosition.Vertical> {
                    valueFormatter = DecimalFormatAxisValueFormatter(DecimalFormat("#.#########"))
                }
            }
            graphChartView.chartScrollSpec = ChartScrollSpec(isScrollEnabled = false)
            graphChartView.setModel(chartEntryModel)
            graphProgressIndicator.visibility = View.GONE
            graphChartView.visibility = View.VISIBLE
        }
    }
}
