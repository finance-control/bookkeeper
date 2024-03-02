package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney

internal data class PositiveMoneyDto(
    val amount: Long,
    val digits: Int = 2,
    val currencyCode: String
) {
    fun toPositiveMoney(): PositiveMoney = PositiveMoney(Currency.byCodeOrThrow(currencyCode), amount, digits)
}

internal fun PositiveMoney.toPositiveMoneyDto() = PositiveMoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.code
)