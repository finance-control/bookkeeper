package com.marsofandrew.bookkeeper.assets

import com.marsofandrew.bookkeeper.properties.Currency


interface CurrencySelection {

    fun select(): List<Currency>
}