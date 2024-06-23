package com.marsofandrew.bookkeeper.assets

import com.marsofandrew.bookkeeper.properties.Currency

data class CurrencyAsset(
    val currency: Currency,
    val title: String,
)
