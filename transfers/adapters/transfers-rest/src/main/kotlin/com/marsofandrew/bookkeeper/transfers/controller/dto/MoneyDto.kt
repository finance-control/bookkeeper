package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.Money

data class MoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
)

fun Money.toMoneyDto() = MoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.code
)