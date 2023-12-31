package com.batuhan.cryptocurrencyapp.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnCloseListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class CryptoCurrencyFragment<X : CryptoCurrencyViewModel, Y : ViewBinding> : Fragment() {

    private var _binding: Y? = null
    val binding get() = _binding!!
    abstract val viewModel: X

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideBinding()
        bind(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    abstract fun bind(binding: Y)

    abstract fun observeViewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun provideBinding(): Y

    open fun onCreateMenuActions(
        onMenuItemClickListener: OnMenuItemClickListener,
        onQueryTextListener: OnQueryTextListener? = null,
        onCloseListener: OnCloseListener? = null
    ) {}

    open fun onMenuItemSelected(
        menuItem: MenuItem
    ): Boolean {
        return true
    }

    open fun clearMenu() {}
}
