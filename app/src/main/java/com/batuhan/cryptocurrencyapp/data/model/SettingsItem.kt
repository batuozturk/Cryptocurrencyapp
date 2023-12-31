package com.batuhan.cryptocurrencyapp.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.batuhan.cryptocurrencyapp.R

@Keep
enum class SettingsItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val routing: SettingsItemRouting
) {

    RATE_US(R.string.rate_us, R.drawable.ic_star_rate_24, SettingsItemRouting.RATE_US),
    SELECT_CURRENCY(
        R.string.select_currency,
        R.drawable.ic_dollar_24,
        SettingsItemRouting.SELECT_CURRENCY
    ),
    OPEN_LIBRARIES(
        R.string.open_libraries,
        R.drawable.ic_code_24,
        SettingsItemRouting.OPEN_LIBARIES
    ),
    CONTACT_US(R.string.contact_us, R.drawable.ic_email_24, SettingsItemRouting.CONTACT_US),
    PRIVACY_POLICY(
        R.string.privacy_policy,
        R.drawable.ic_article_24,
        SettingsItemRouting.PRIVACY_POLICY
    ),
    SELECT_LANGUAGE(
        R.string.select_language,
        R.drawable.ic_language_24,
        SettingsItemRouting.SELECT_LANGUAGE
    )
}

@Keep
enum class SettingsItemRouting {
    RATE_US,
    OPEN_LIBARIES,
    CONTACT_US,
    PRIVACY_POLICY,
    SELECT_CURRENCY,
    SELECT_LANGUAGE
}

fun generateSettingsList(): List<SettingsItem> {
    val list = mutableListOf<SettingsItem>()
    list.add(SettingsItem.SELECT_LANGUAGE)
    list.add(SettingsItem.RATE_US)
    list.add(SettingsItem.SELECT_CURRENCY)
    list.add(SettingsItem.CONTACT_US)
    list.add(SettingsItem.OPEN_LIBRARIES)
    list.add(SettingsItem.PRIVACY_POLICY)
    return list
}
