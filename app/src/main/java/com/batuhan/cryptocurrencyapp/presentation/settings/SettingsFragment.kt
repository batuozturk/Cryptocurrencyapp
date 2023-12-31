package com.batuhan.cryptocurrencyapp.presentation.settings

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.cryptocurrencyapp.MainActivity
import com.batuhan.cryptocurrencyapp.core.CryptoCurrencyFragment
import com.batuhan.cryptocurrencyapp.data.model.SettingsItemRouting
import com.batuhan.cryptocurrencyapp.databinding.FragmentSettingsBinding
import com.batuhan.cryptocurrencyapp.presentation.settings.adapter.SettingsAdapter
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.opensourcelibrary.OpenSourceLibraryBottomSheetFragment
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency.SelectCurrencyBottomSheetFragment
import com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage.SelectLanguageBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : CryptoCurrencyFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val viewModel: SettingsViewModel by viewModels()

    private var adapter: SettingsAdapter? = null

    override fun observeViewModel() {
        viewModel.run {
            settingsList.observe(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
            routing.observe(viewLifecycleOwner) {
                when (it) {
                    SettingsItemRouting.RATE_US -> {
                        val tabIntent = CustomTabsIntent.Builder().build()
                        tabIntent.launchUrl(
                            requireContext(),
                            Uri.parse("https://play.google.com/store/apps/details?id=com.batuhan.cryptocurrencyapp")
                        )
                    }

                    SettingsItemRouting.OPEN_LIBARIES -> {
                        OpenSourceLibraryBottomSheetFragment().show(childFragmentManager, "Open Libraries")
                    }

                    SettingsItemRouting.CONTACT_US -> {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("mailto:")
                        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("batuoztrk99@gmail.com"))
                        intent.putExtra(Intent.EXTRA_SUBJECT, "About CryptoCurrency")

                        startActivity(Intent.createChooser(intent, "Send email via"))
                    }

                    SettingsItemRouting.PRIVACY_POLICY -> {
                        val tabIntent = CustomTabsIntent.Builder().build()
                        tabIntent.launchUrl(
                            requireContext(),
                            Uri.parse("https://sites.google.com/view/kriptoparauygulamasi/ana-sayfa")
                        )
                    }

                    SettingsItemRouting.SELECT_CURRENCY -> {
                        SelectCurrencyBottomSheetFragment().show(
                            childFragmentManager,
                            "Select Currency"
                        )
                    }

                    SettingsItemRouting.SELECT_LANGUAGE -> {
                        SelectLanguageBottomSheetFragment {
                            (requireActivity() as? MainActivity)?.endApplication()
                        }.show(childFragmentManager, "Select Language")
                    }
                }
            }
        }
    }

    override fun provideBinding(): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun bind(binding: FragmentSettingsBinding) {
        binding.run {
            adapter = SettingsAdapter()
            adapter?.eventHandler = viewModel
            settingsList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            settingsList.adapter = adapter
        }
    }
}
