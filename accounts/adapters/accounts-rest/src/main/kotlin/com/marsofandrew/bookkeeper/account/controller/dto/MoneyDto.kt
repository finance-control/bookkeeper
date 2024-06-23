package com.marsofandrew.bookkeeper.account.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import java.math.BigDecimal

internal data class MoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
) {

    fun toMoney() = Money(
        currency = Currency.valueOf(currencyCode),
        amount = BigDecimal(amount).movePointLeft(digits)
    )
}

internal fun Money.toMoneyDto() = MoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.name
)
