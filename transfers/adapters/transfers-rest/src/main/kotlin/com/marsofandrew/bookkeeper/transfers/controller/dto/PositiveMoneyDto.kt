package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.validateFiled

internal data class PositiveMoneyDto(
    val amount: Long,
    val digits: Int,
    val currencyCode: String
) {
    init {
        validateFiled(amount > 0) { "amount is zero or negative" }
    }

    fun toPositiveMoney(): PositiveMoney = PositiveMoney(Currency.valueOf(currencyCode), amount, digits)
}

internal fun PositiveMoney.toPositiveMoneyDto() = PositiveMoneyDto(
    amount = amount.movePointRight(amount.scale()).longValueExact(),
    digits = amount.scale(),
    currencyCode = currency.name
)