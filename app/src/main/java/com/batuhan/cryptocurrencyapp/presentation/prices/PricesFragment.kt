package com.batuhan.cryptocurrencyapp.presentation.prices

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnCloseListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentPricesBinding
import com.batuhan.cryptocurrencyapp.presentation.pages.ContainerFragment
import com.batuhan.cryptocurrencyapp.presentation.prices.adapter.PricesAdapter
import com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet.PricesFilterBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PricesFragment : CryptoCurrencyFragment<PricesViewModel, FragmentPricesBinding>() {
    override val viewModel: PricesViewModel by viewModels()

    private var adapter: PricesAdapter? = null

    override fun observeViewModel() {
        viewModel.run {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    prices.collectLatest {
                        adapter?.submitData(viewLifecycleOwner.lifecycle, it)
                    }
                }
            }
            expandedId.observe(viewLifecycleOwner) {
                adapter?.refresh()
            }
            historyItems.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter?.refresh()
                }
            }
            detailRouteId.observe(viewLifecycleOwner) {
                (parentFragment as ContainerFragment).routeToDetailScreen(it)
            }
            showSnackbar.observe(viewLifecycleOwner) {
                (parentFragment as ContainerFragment).showSnackbar(it)
            }
        }
    }

    override fun provideBinding(): FragmentPricesBinding {
        return FragmentPricesBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentPricesBinding) {
        binding.run {
            adapter = PricesAdapter()
            adapter?.eventHandler = viewModel
            pricesList.adapter = adapter
            (pricesList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            pricesList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            onCreateMenuActions(this@PricesFragment::onMenuItemSelected)
        }
    }

    override fun onResume() {
        super.onResume()
        onCreateMenuActions(this@PricesFragment::onMenuItemSelected)
        viewModel.initializeJob()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearJob()
        viewModel.showSnackbar.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    override fun onCreateMenuActions(
        onMenuItemClickListener: OnMenuItemClickListener,
        onQueryTextListener: OnQueryTextListener?,
        onCloseListener: SearchView.OnCloseListener?
    ) {
        val queryTextListener = object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // no-op
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                viewModel.setFilterText(newText ?: "")
                return true
            }
        }
        val onCloseListener = OnCloseListener {
            adapter?.refresh()
            true
        }
        (parentFragment as ContainerFragment).onCreateMenuActions(
            onMenuItemClickListener,
            queryTextListener,
            onCloseListener
        )
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.title) {
            "Filter" -> PricesFilterBottomSheetFragment(onDismiss = {
                viewModel.clearJob()
                viewModel.clearExpandedId()
                viewModel.clearHistoryList()
                adapter?.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                viewModel.setSelectedFilter(it)
                viewModel.startRepeatingJob()
            }, selectedFilter = viewModel.filterPair.value.first.name).show(
                childFragmentManager,
                "Filter"
            )
        }
        return (parentFragment as ContainerFragment).onMenuItemSelected(menuItem)
    }
}
