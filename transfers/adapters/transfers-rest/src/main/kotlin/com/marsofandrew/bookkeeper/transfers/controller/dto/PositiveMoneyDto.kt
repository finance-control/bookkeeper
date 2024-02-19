package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney

data class PositiveMoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
) {
    init {
        check(amount > 0) { "amount is zero or negative" }
    }

    fun toPositiveMoney(): PositiveMoney = PositiveMoney(Currency.byCodeOrThrow(currencyCode), amount, digits)
}

fun PositiveMoney.toPositiveMoneyDto() = PositiveMoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.code
)