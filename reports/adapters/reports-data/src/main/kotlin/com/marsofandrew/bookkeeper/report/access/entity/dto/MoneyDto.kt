package com.marsofandrew.bookkeeper.report.access.entity.dto

import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money

internal data class MoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
)

internal fun BaseMoney.toMoneyDto() = MoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.code
)

internal fun MoneyDto.toMoney() = Money(
    currency = Currency.byCodeOrThrow(currencyCode),
    amount = amount,
    decimals = digits
)