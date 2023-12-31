package com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceBottomSheetBinding
import com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet.adapter.NewsSourceAdapter

class NewsSourceBottomSheetFragment(
    private val onDismiss: (String) -> Unit,
    private val selectedNewsSource: String
) :
    CryptoCurrencyBottomSheetFragment<NewsSourceBottomSheetViewModel, NewsSourceBottomSheetBinding>() {
    override val viewModel: NewsSourceBottomSheetViewModel by viewModels()

    private var adapter: NewsSourceAdapter? = null

    override fun bind(binding: NewsSourceBottomSheetBinding) {
        binding.run {
            adapter = NewsSourceAdapter()
            adapter?.eventHandler = viewModel
            newsSourceList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            newsSourceList.adapter = adapter
        }
    }

    override fun provideBinding(): NewsSourceBottomSheetBinding {
        return NewsSourceBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun observeViewModel() {
        viewModel.run {
            setSelectedNewsSource(selectedNewsSource)
            newsSourceValues.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
            onDismiss.observe(viewLifecycleOwner) {
                this@NewsSourceBottomSheetFragment.onDismiss.invoke(it)
                this@NewsSourceBottomSheetFragment.dismiss()
            }
        }
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }
}
