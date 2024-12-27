package com.marsofandrew.bookkeeper.assets.impl

import com.marsofandrew.bookkeeper.assets.CurrencyAsset
import com.marsofandrew.bookkeeper.assets.CurrencySelection
import com.marsofandrew.bookkeeper.properties.Currency

class CurrencySelectionImpl : CurrencySelection {

    override fun select(): List<CurrencyAsset> = listOf(
        CurrencyAsset(Currency.EUR, "Euro"),
        CurrencyAsset(Currency.RUB, "Russian Ruble"),
        CurrencyAsset(Currency.USD, "US Dollar"),
        CurrencyAsset(Currency.GBP, "GB Pound"),
        CurrencyAsset(Currency.GEL, "Georgian Lari"),
        CurrencyAsset(Currency.AED, "United Arab Emirates Dirham"),
        CurrencyAsset(Currency.RSD, "Republic of Serbia Dinar")
    )
}