package com.batuhan.cryptocurrencyapp.presentation.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.batuhan.cryptocurrencyapp.MainActivity
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyItemData
import com.batuhan.cryptocurrencyapp.data.model.CryptoCurrencyState
import com.batuhan.cryptocurrencyapp.data.model.HistoryItem
import com.batuhan.cryptocurrencyapp.databinding.FragmentCurrencyDetailBinding
import com.batuhan.cryptocurrencyapp.presentation.detail.pages.DetailTabPagerAdapter
import com.batuhan.cryptocurrencyapp.presentation.prices.adapter.getColor
import com.google.android.material.tabs.TabLayoutMediator
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.views.scroll.ChartScrollSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyDetailFragment :
    CryptoCurrencyFragment<CurrencyDetailViewModel, FragmentCurrencyDetailBinding>() {
    override val viewModel: CurrencyDetailViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun observeViewModel() {
        viewModel.run {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    price.collectLatest {
                        updatePriceCard(it)
                        (binding.detailChartView.chart as LineChart).apply {
                            lines[0].apply {
                                lineColor = requireContext().getColor(getColor(it.state))
                            }
                        }
                    }
                }
            }
            historyItems.observe(viewLifecycleOwner) {
                it?.let {
                    updateGraphCard(it)
                }
            }
        }
    }

    override fun provideBinding(): FragmentCurrencyDetailBinding {
        return FragmentCurrencyDetailBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentCurrencyDetailBinding) {
        binding.run {
            val tabTexts = listOf(R.string.details, R.string.markets)
            detailPager.adapter = DetailTabPagerAdapter(viewModel.currencyId, this@CurrencyDetailFragment)
            TabLayoutMediator(detailTabLayout, detailPager) { tab, position ->
                tab.text = requireContext().getString(tabTexts[position])
            }
        }.attach()
    }

    fun updatePriceCard(item: CryptoCurrencyItemData) {
        binding.detailName.text = item.name
        binding.detailChange.text =
            item.change?.toBigDecimal()?.toPlainString()?.substring(
            0,
            Integer.max(item.change.length - 9, 5)
        ) + "%"
        binding.detailPrice.text = item.price?.toBigDecimal()?.toPlainString()
            ?.substring(0, Integer.min(11, item.price.length))
        val drawable = when (item.state) {
            CryptoCurrencyState.PLUS -> R.drawable.ic_circle_plus_24
            CryptoCurrencyState.MINUS -> R.drawable.ic_circle_minus_24
            CryptoCurrencyState.UNCH -> R.drawable.ic_circle_unch_24
        }
        binding.detailState.setImageResource(drawable)
        val favoriteDrawable =
            if (item.isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
        binding.detailFavoriteItem.setImageResource(favoriteDrawable)
        binding.detailFavoriteItem.setOnClickListener {
            viewModel.onFavorite(item.id ?: return@setOnClickListener)
        }
        binding.detailFullScreenItem.setOnClickListener {
            (requireActivity() as? MainActivity)?.navigateToGraphScreen(viewModel.currencyId)
        }
    }

    fun updateGraphCard(list: List<HistoryItem>) {
        binding.run {
            detailProgressIndicator.visibility = View.GONE
            val items = (
                list.filter { it.priceUsd != null }
                    .map { it.priceUsd!!.toFloat() }
                ).toTypedArray()
            val chartEntryModel = entryModelOf(*items)
            (detailChartView.chart as LineChart).apply {
                axisValuesOverrider =
                    object : AxisValuesOverrider<ChartEntryModel> {
                        override fun getMinY(model: ChartEntryModel) = model.minY
                        override fun getMaxY(model: ChartEntryModel) = model.maxY
                    }
            }
            detailChartView.chartScrollSpec = ChartScrollSpec(isScrollEnabled = false)
            detailChartView.setModel(chartEntryModel)
            detailChartView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startRepeatingJob()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearJob()
    }
}
