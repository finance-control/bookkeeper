package com.marsofandrew.bookkeeper.properties.util

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney

fun PositiveMoney.toMoney() = Money(currency, amount)

fun Money.toPositiveMoney() = PositiveMoney(currency, amount)