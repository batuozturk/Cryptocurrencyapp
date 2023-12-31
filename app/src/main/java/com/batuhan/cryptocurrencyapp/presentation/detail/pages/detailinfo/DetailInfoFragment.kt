package com.batuhan.cryptocurrencyapp.presentation.detail.pages.detailinfo

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentDetailInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailInfoFragment(private val currencyId: String) :
    CryptoCurrencyFragment<DetailInfoViewModel, FragmentDetailInfoBinding>() {

    private var adapter: DetailInfoAdapter? = null
    override val viewModel: DetailInfoViewModel by viewModels()

    override fun bind(binding: FragmentDetailInfoBinding) {
        viewModel.initCurrencyId(currencyId)
        binding.run {
            detailInfoList.itemAnimator = null
            detailInfoList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = DetailInfoAdapter()
            detailInfoList.adapter = adapter
        }
    }

    override fun observeViewModel() {
        viewModel.run {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    priceDetailFlow.collect {
                        adapter?.submitList(it)
                    }
                }
            }
        }
    }

    override fun provideBinding(): FragmentDetailInfoBinding {
        return FragmentDetailInfoBinding.inflate(LayoutInflater.from(requireContext()))
    }
}
