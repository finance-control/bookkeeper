package com.marsofandrew.bookkeeper.properties

import java.math.BigDecimal

interface BaseMoney {
    val currency: Currency
    val amount: BigDecimal
}
