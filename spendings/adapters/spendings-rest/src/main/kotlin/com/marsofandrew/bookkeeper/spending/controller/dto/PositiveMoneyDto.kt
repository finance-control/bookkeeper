package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney

internal data class PositiveMoneyDto(
    val amount: Long,
    val digits: Int = 2,
    val currencyCode: String
) {
    fun toPositiveMoney(): PositiveMoney = PositiveMoney(Currency.valueOf(currencyCode), amount, digits)
}

internal fun PositiveMoney.toPositiveMoneyDto() = PositiveMoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.name
)
