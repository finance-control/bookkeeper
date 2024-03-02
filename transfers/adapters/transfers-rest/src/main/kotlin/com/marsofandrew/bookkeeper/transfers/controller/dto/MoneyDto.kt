package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.Money

internal data class MoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
)

internal fun Money.toMoneyDto() = MoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.code
)