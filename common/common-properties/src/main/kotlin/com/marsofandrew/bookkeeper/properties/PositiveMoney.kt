package com.marsofandrew.bookkeeper.properties

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.util.normalize
import java.math.BigDecimal
import java.util.Objects

data class PositiveMoney(
    override val currency: Currency,
    override val amount: BigDecimal
) : BaseMoney {

    constructor(currency: Currency, amount: Long, decimals: Int = 2) : this(
        currency,
        BigDecimal(amount).movePointLeft(decimals)
    )

    init {
        validateFiled(amount.toDouble() > 0) { "amount of money is negative or zero" }
    }

    override fun plus(money: BaseMoney): PositiveMoney {
        require(money.currency == currency) { "you could add only money with same currency" }

        return PositiveMoney(
            currency = currency,
            amount = amount + money.amount
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PositiveMoney) return false

        return currency == other.currency && amount.compareTo(other.amount) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(currency, amount.normalize())
    }
}
