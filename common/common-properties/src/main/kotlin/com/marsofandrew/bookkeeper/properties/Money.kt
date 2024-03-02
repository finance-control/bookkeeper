package com.marsofandrew.bookkeeper.properties

import java.math.BigDecimal

data class Money(
    override val currency: Currency,
    override val amount: BigDecimal
) : BaseMoney {
    constructor(currency: Currency, amount: Long, decimals: Int = 2) : this(
        currency,
        BigDecimal(amount).movePointLeft(decimals)
    )

    override fun plus(money: BaseMoney): Money {
        check(money.currency == currency) { "you could add only money with same currency" }

        return Money(
            currency = currency,
            amount = amount + money.amount
        )
    }

    operator fun minus(money: BaseMoney): Money {
        check(money.currency == currency) { "you could add only money with same currency" }

        return Money(
            currency = currency,
            amount = amount - money.amount
        )
    }
}