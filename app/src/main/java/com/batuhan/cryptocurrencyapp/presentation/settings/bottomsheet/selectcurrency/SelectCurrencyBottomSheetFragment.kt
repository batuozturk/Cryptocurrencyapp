package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceBottomSheetBinding
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency.adapter.SelectCurrencyAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCurrencyBottomSheetFragment :
    CryptoCurrencyBottomSheetFragment<SelectCurrencyViewModel, NewsSourceBottomSheetBinding>() {

    override val viewModel: SelectCurrencyViewModel by viewModels()

    private var adapter: SelectCurrencyAdapter? = null

    override fun provideBinding(): NewsSourceBottomSheetBinding {
        return NewsSourceBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun observeViewModel() {
        viewModel.run {
            currencyList.observe(viewLifecycleOwner) {
                it?.let {
                    adapter?.submitList(it)
                }
            }
            onDismiss.observe(viewLifecycleOwner) {
                this@SelectCurrencyBottomSheetFragment.dismiss()
            }
        }
    }

    override fun bind(binding: NewsSourceBottomSheetBinding) {
        binding.run {
            text.text = requireContext().getString(R.string.select_currency)
            newsSourceList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = SelectCurrencyAdapter().apply {
                eventHandler = viewModel
            }
            newsSourceList.adapter = adapter
        }
    }
}
