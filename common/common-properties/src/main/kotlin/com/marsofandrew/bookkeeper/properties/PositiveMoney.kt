package com.marsofandrew.bookkeeper.properties

import java.math.BigDecimal

data class PositiveMoney(
    override val currency: Currency,
    override val amount: BigDecimal
) : BaseMoney {

    constructor(currency: Currency, amount: Long, decimals: Int = 2) : this(
        currency,
        BigDecimal(amount).movePointLeft(decimals)
    )

    init {
        check(amount.toDouble() > 0) { "amount of money is negative or zero" }
    }
}
