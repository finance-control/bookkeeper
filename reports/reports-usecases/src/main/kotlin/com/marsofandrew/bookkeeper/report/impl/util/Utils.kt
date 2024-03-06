package com.marsofandrew.bookkeeper.report.impl.util

import com.marsofandrew.bookkeeper.base.utils.summarize
import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import java.math.BigDecimal

@Suppress("UNCHECKED_CAST")
internal fun <T : BaseMoney> List<T>.addMoney(money: BaseMoney): List<T> {
    return (this + money)
        .groupBy { it.currency }
        .mapValues { (_, moneys) -> moneys.reduce { acc, positiveMoney -> acc + positiveMoney } }
        .values
        .map { it as T }
        .toList()
}

internal val Transfer.totalMoney: List<Money>
    get() = summarize(
        listOf(Money(received.currency, received.amount)),
        send.let { listOf(Money(it.currency, -it.amount)) } ?: emptyList()
    ).toList()

internal operator fun PositiveMoney.unaryMinus(): Money = Money(currency, -amount)
internal operator fun Money.unaryMinus(): Money = copy(amount = -amount)

internal fun PositiveMoney.toMoney() = Money(currency, amount)

internal fun Money.toPositiveMoney() = PositiveMoney(currency, amount)

internal fun List<PositiveMoney>.addNegative(money: Money): List<PositiveMoney> =
    map { it.toMoney() }
        .addMoney(money)
        .filter { it.amount != BigDecimal.ZERO }
        .map { it.toPositiveMoney() }