package com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceBottomSheetBinding
import com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet.adapter.PricesFilterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PricesFilterBottomSheetFragment(
    private val onDismiss: (String) -> Unit,
    private val selectedFilter: String
) :
    CryptoCurrencyBottomSheetFragment<PricesFilterViewModel, NewsSourceBottomSheetBinding>() {
    override val viewModel: PricesFilterViewModel by viewModels()

    private var adapter: PricesFilterAdapter? = null

    override fun provideBinding(): NewsSourceBottomSheetBinding {
        return NewsSourceBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun observeViewModel() {
        viewModel.run {
            filters.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
            dismiss.observe(viewLifecycleOwner) {
                onDismiss(it)
                this@PricesFilterBottomSheetFragment.dismiss()
            }
            initFilterTypes(selectedFilter)
        }
    }

    override fun bind(binding: NewsSourceBottomSheetBinding) {
        binding.run {
            text.text = requireContext().getString(R.string.sort)
            newsSourceList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = PricesFilterAdapter()
            adapter?.eventHandler = viewModel
            newsSourceList.adapter = adapter
        }
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }
}
