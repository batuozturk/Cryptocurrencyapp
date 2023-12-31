package com.batuhan.cryptocurrencyapp.presentation.detail.pages.exchanges

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentDetailInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangesFragment(private val currencyId: String) :
    CryptoCurrencyFragment<ExchangesViewModel, FragmentDetailInfoBinding>() {
    override val viewModel: ExchangesViewModel by viewModels()

    private var adapter: ExchangeAdapter? = null

    override fun observeViewModel() {
        viewModel.run {
            exchangesList.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
        }
    }

    override fun provideBinding(): FragmentDetailInfoBinding {
        return FragmentDetailInfoBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentDetailInfoBinding) {
        viewModel.initCurrencyId(currencyId)
        binding.run {
            detailInfoList.itemAnimator = null
            detailInfoList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = ExchangeAdapter()
            detailInfoList.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startRepeatingJob()
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelJob()
    }
}
