package com.marsofandrew.bookkeeper.properties

import com.marsofandrew.bookkeeper.properties.util.normalize
import java.math.BigDecimal
import java.util.Objects

data class Money(
    override val currency: Currency,
    override val amount: BigDecimal
) : BaseMoney {
    constructor(currency: Currency, amount: Long, decimals: Int = 2) : this(
        currency,
        BigDecimal(amount).movePointLeft(decimals)
    )

    override fun plus(money: BaseMoney): Money {
        require(money.currency == currency) { "you could add only money with same currency" }

        return Money(
            currency = currency,
            amount = amount + money.amount
        )
    }

    operator fun minus(money: BaseMoney): Money {
        require(money.currency == currency) { "you could add only money with same currency" }

        return Money(
            currency = currency,
            amount = amount - money.amount
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Money) return false

        return currency == other.currency && amount.compareTo(other.amount) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(currency, amount.normalize())
    }

    fun isZero() = amount.compareTo(BigDecimal.ZERO) == 0

    companion object {
        fun zero(currency: Currency) = Money(currency, BigDecimal.ZERO)
    }
}