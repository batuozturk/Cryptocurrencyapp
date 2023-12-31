package com.batuhan.cryptocurrencyapp.presentation.detail.pages

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.batuhan.cryptocurrencyapp.presentation.detail.pages.detailinfo.DetailInfoFragment
import com.batuhan.cryptocurrencyapp.presentation.detail.pages.exchanges.ExchangesFragment

class DetailTabPagerAdapter(private val currencyId: String, fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private const val PAGE_ONE = 0
        private const val PAGE_TWO = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_ONE -> DetailInfoFragment(currencyId)
            PAGE_TWO -> ExchangesFragment(currencyId)
            else -> Fragment()
        }
    }
}
