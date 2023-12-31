package com.batuhan.cryptocurrencyapp.presentation.converter

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentCurrencyConverterBinding
import com.batuhan.cryptocurrencyapp.presentation.converter.bottomsheet.SelectFavoriteBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyConverterFragment() :
    CryptoCurrencyFragment<CurrencyConverterViewModel, FragmentCurrencyConverterBinding>() {

    override val viewModel: CurrencyConverterViewModel by viewModels()

    override fun bind(binding: FragmentCurrencyConverterBinding) {
        binding.run {
            numberKeyboard.setListener(viewModel)
            input1.requestFocus()
            input1.setOnFocusChangeListener { _, focused ->
                viewModel.setSelectedCursorIndex(input1.text?.length ?: 0)
            }
            input1.eventHandler = viewModel
            input1.showSoftInputOnFocus = false
            currencySelectInput1.showSoftInputOnFocus = false
            currencySelectInput2.showSoftInputOnFocus = false
            currencySelectInput3.showSoftInputOnFocus = false
            currencySelectInput4.showSoftInputOnFocus = false
            currencySelectInput1.setOnClickListener {
                SelectFavoriteBottomSheetFragment(0) {
                    viewModel.setCurrencyField(0, it)
                }.show(childFragmentManager, "First Currency")
            }
            currencySelectInput2.setOnClickListener {
                SelectFavoriteBottomSheetFragment(1) {
                    viewModel.setCurrencyField(1, it)
                }.show(childFragmentManager, "First Currency")
            }
            currencySelectInput3.setOnClickListener {
                SelectFavoriteBottomSheetFragment(2) {
                    viewModel.setCurrencyField(2, it)
                }.show(childFragmentManager, "First Currency")
            }
            currencySelectInput4.setOnClickListener {
                SelectFavoriteBottomSheetFragment(3) {
                    viewModel.setCurrencyField(3, it)
                }.show(childFragmentManager, "First Currency")
            }
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.converterFavorites.collectLatest {
                        if (it.isNotEmpty()) {
                            binding.input2.setText(it.get(1))
                            binding.input3.setText(it.get(2))
                            binding.input4.setText(it.get(3))
                        }
                    }
                }
            }
        }
    }

    override fun observeViewModel() {
        viewModel.run {
            firstField.observe(viewLifecycleOwner) {
                binding.input1.setText(it)
                setSelectedCursorIndex(it.length)
                binding.input1.setSelection(it.length)
            }
            firstCurrency.observe(viewLifecycleOwner) {
                binding.currencySelectInput1.setText(it.symbol)
            }
            secondCurrency.observe(viewLifecycleOwner) {
                binding.currencySelectInput2.setText(it.symbol)

            }
            thirdCurrency.observe(viewLifecycleOwner) {
                binding.currencySelectInput3.setText(it.symbol)

            }
            fourthCurrency.observe(viewLifecycleOwner) {
                binding.currencySelectInput4.setText(it.symbol)

            }
        }
    }

    override fun provideBinding(): FragmentCurrencyConverterBinding {
        return FragmentCurrencyConverterBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun onResume() {
        super.onResume()
        viewModel.initJob()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearJob()
    }
}
