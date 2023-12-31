package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceBottomSheetBinding
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage.adapter.SelectLanguageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectLanguageBottomSheetFragment(private val onSuccess: () -> Unit) :
    CryptoCurrencyBottomSheetFragment<SelectLanguageViewModel, NewsSourceBottomSheetBinding>() {
    override val viewModel: SelectLanguageViewModel by viewModels()

    private var adapter: SelectLanguageAdapter? = null

    override fun provideBinding(): NewsSourceBottomSheetBinding {
        return NewsSourceBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun observeViewModel() {
        viewModel.run {
            languageList.observe(viewLifecycleOwner) {
                it?.let {
                    adapter?.submitList(it)
                }
            }
            onDismiss.observe(viewLifecycleOwner) {
                requireContext().getSharedPreferences("lang_pref", AppCompatActivity.MODE_PRIVATE).edit().putString("lang", it.locale.language).apply()
                onSuccess.invoke()
                this@SelectLanguageBottomSheetFragment.dismiss()
            }
        }
    }

    override fun bind(binding: NewsSourceBottomSheetBinding) {
        binding.run {
            text.text = "Select Language"
            newsSourceList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = SelectLanguageAdapter().apply {
                eventHandler = viewModel
            }
            newsSourceList.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        val langCode =
            requireContext().getSharedPreferences("lang_pref", AppCompatActivity.MODE_PRIVATE)
                .getString("lang", "tr") ?: "tr"
        viewModel.generateLocaleData(langCode)
    }
}
