package com.marsofandrew.bookkeeper.assets


interface CurrencySelection {

    fun select(): List<CurrencyAsset>
}