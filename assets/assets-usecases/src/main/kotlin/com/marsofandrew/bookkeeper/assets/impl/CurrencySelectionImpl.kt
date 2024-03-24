package com.marsofandrew.bookkeeper.assets.impl

import com.marsofandrew.bookkeeper.assets.CurrencySelection
import com.marsofandrew.bookkeeper.properties.Currency

class CurrencySelectionImpl : CurrencySelection {

    override fun select(): List<Currency> = Currency.entries
}