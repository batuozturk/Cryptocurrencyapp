package com.batuhan.cryptocurrencyapp.presentation.pages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.batuhan.cryptocurrencyapp.presentation.converter.CurrencyConverterFragment
import com.batuhan.cryptocurrencyapp.presentation.news.NewsFragment
import com.batuhan.cryptocurrencyapp.presentation.prices.PricesFragment
import com.batuhan.cryptocurrencyapp.presentation.settings.SettingsFragment

class CryptoCurrencyPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    companion object {
        private const val PAGE_ONE = 0
        private const val PAGE_TWO = 1
        private const val PAGE_THREE = 2
        private const val PAGE_FOUR = 3
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_ONE -> PricesFragment()
            PAGE_TWO -> NewsFragment()
            PAGE_THREE -> CurrencyConverterFragment()
            PAGE_FOUR -> SettingsFragment()
            else -> Fragment()
        }
    }
}
