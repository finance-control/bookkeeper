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
}