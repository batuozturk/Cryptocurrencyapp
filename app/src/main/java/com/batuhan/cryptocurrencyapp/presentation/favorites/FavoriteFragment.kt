package com.batuhan.cryptocurrencyapp.presentation.favorites

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : CryptoCurrencyFragment<FavoriteViewModel, FragmentFavoriteBinding>() {
    override val viewModel: FavoriteViewModel by viewModels()

    override fun observeViewModel() {
        viewModel.run {

        }
    }

    override fun provideBinding(
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentFavoriteBinding) {
        binding.run {

        }
    }
}
