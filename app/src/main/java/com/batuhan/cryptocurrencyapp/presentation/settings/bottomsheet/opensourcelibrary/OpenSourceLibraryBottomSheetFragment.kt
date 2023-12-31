package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.opensourcelibrary

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentOpenSourceLibraryBinding

class OpenSourceLibraryBottomSheetFragment :
    CryptoCurrencyBottomSheetFragment<OpenSourceLibraryViewModel, FragmentOpenSourceLibraryBinding>() {
    override val viewModel: OpenSourceLibraryViewModel by viewModels()

    override fun provideBinding(): FragmentOpenSourceLibraryBinding {
        return FragmentOpenSourceLibraryBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun observeViewModel() {
        viewModel.run {
        }
    }

    override fun bind(binding: FragmentOpenSourceLibraryBinding) {
        binding.run {
        }
    }
}
