package com.marsofandrew.bookkeeper.report.controller.dto

import com.marsofandrew.bookkeeper.properties.BaseMoney

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
