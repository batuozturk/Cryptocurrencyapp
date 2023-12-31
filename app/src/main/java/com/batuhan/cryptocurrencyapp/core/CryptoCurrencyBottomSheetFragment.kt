package com.batuhan.cryptocurrencyapp.core

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class CryptoCurrencyBottomSheetFragment<X : CryptoCurrencyViewModel, Y : ViewBinding> () :
    BottomSheetDialogFragment() {

    abstract val viewModel: X

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = provideBinding()
        bind(binding)
        observeViewModel()
        return binding.root
    }

    abstract fun bind(binding: Y)

    abstract fun provideBinding(): Y

    abstract fun observeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }
}
