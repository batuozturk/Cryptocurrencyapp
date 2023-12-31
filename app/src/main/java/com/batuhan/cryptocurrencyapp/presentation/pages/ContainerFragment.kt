package com.batuhan.cryptocurrencyapp.presentation.pages

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.core.SnackbarData
import com.batuhan.cryptocurrencyapp.databinding.ContainerFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerFragment : CryptoCurrencyFragment<ContainerViewModel, ContainerFragmentBinding>() {
    override val viewModel: ContainerViewModel by viewModels()

    override fun observeViewModel() {
        viewModel.run {
        }
    }

    override fun provideBinding(): ContainerFragmentBinding {
        return ContainerFragmentBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: ContainerFragmentBinding) {
        binding.run {
            binding.viewPager.run {
                adapter = CryptoCurrencyPagerAdapter(this@ContainerFragment)
                isUserInputEnabled = false
            }
            inflateMenu(R.menu.menu_prices)
            binding.viewPager.currentItem = 0
            binding.bottomNavigation.setOnItemSelectedListener { model ->
                when (model.itemId) {
                    R.id.tab_prices -> {
                        inflateMenu(R.menu.menu_prices)
                        binding.viewPager.currentItem = 0
                    }

                    R.id.tab_news -> {
                        inflateMenu(R.menu.menu_news)
                        binding.viewPager.currentItem = 1
                    }

                    R.id.tab_converter -> {
                        clearMenu()
                        binding.viewPager.currentItem = 2
                    }

                    R.id.tab_settings -> {
                        clearMenu()
                        binding.viewPager.currentItem = 3
                    }
                }
                true
            }
        }
    }

    fun inflateMenu(@MenuRes menuRes: Int) {
        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(menuRes)
        (binding.toolbar.menu.findItem(R.id.search)?.actionView as? SearchView)?.apply {
            background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.search_view_border)
            setIconifiedByDefault(false)
        }
    }

    override fun onCreateMenuActions(
        onMenuItemClickListener: OnMenuItemClickListener,
        onQueryTextListener: SearchView.OnQueryTextListener?,
        onCloseListener: SearchView.OnCloseListener?
    ) {
        binding.toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
        onQueryTextListener?.let {
            (binding.toolbar.menu.findItem(R.id.search)?.actionView as? SearchView)?.apply {
                setOnQueryTextListener(it)
                setOnCloseListener(onCloseListener)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return super.onMenuItemSelected(menuItem)
    }

    override fun clearMenu() {
        binding.toolbar.menu.clear()
    }

    fun routeToDetailScreen(id: String) {
        findNavController().navigate(
            ContainerFragmentDirections.actionContainerFragmentToCurrencyDetailFragment(
                id
            )
        )
    }

    fun showSnackbar(snackbarData: SnackbarData) {
        Snackbar.make(
            requireContext(),
            binding.root,
            getString(snackbarData.title),
            Snackbar.LENGTH_INDEFINITE
        ).run {
            val snackbarActionTextView =
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            snackbarActionTextView.isAllCaps = false
            setAction(
                snackbarData.actionTitle?.let { resId -> getString(resId) }
            ) {
                snackbarData.action?.invoke()
                dismiss()
            }
            setActionTextColor(requireContext().getColor(snackbarData.textColor))
            setTextColor(requireContext().getColor(snackbarData.textColor))
            setBackgroundTint(requireContext().getColor(snackbarData.backgroundColor))
            setTextMaxLines(2)
            anchorView = binding.bottomNavigation
            show()
        }
    }
}
