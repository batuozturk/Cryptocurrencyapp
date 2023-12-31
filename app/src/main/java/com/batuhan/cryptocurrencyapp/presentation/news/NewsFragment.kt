package com.batuhan.cryptocurrencyapp.presentation.news

import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.databinding.FragmentNewsBinding
import com.batuhan.cryptocurrencyapp.presentation.news.adapter.NewsAdapter
import com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet.NewsSourceBottomSheetFragment
import com.batuhan.cryptocurrencyapp.presentation.pages.ContainerFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : CryptoCurrencyFragment<NewsViewModel, FragmentNewsBinding>() {
    override val viewModel: NewsViewModel by viewModels()

    private var adapter: NewsAdapter? = null

    override fun observeViewModel() {
        viewModel.run {
            rssItems.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
            newsSource.observe(viewLifecycleOwner) {
            }
            selectedNewsUrl.observe(viewLifecycleOwner) {
                val tabsIntent = CustomTabsIntent.Builder().build()
                tabsIntent.launchUrl(requireContext(), Uri.parse(it))
            }
            showSnackbar.observe(viewLifecycleOwner){ snackbarData ->
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(snackbarData.title),
                    Snackbar.LENGTH_INDEFINITE
                ).run {
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
                    anchorView = parentFragment?.view?.rootView?.findViewById(R.id.bottom_navigation)
                    show()
                }
            }
        }
    }

    override fun provideBinding(): FragmentNewsBinding {
        return FragmentNewsBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentNewsBinding) {
        binding.run {
            adapter = NewsAdapter()
            adapter?.eventHandler = viewModel
            newsList.adapter = adapter
            (newsList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            newsList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            onCreateMenuActions(this@NewsFragment::onMenuItemSelected)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    override fun onCreateMenuActions(
        onMenuItemClickListener: OnMenuItemClickListener,
        onQueryTextListener: SearchView.OnQueryTextListener?,
        onCloseListener: SearchView.OnCloseListener?
    ) {
        (parentFragment as ContainerFragment).onCreateMenuActions(onMenuItemClickListener)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.title) {
            "News Source" -> {
                NewsSourceBottomSheetFragment(
                    {
                        viewModel.setSelectedNewsSource(it)
                    },
                    viewModel.newsSource.value!!.name
                ).show(childFragmentManager, "News Source")
            }
        }
        return (parentFragment as ContainerFragment).onMenuItemSelected(menuItem)
    }

    override fun onResume() {
        super.onResume()
        onCreateMenuActions(this@NewsFragment::onMenuItemSelected)
    }
}
