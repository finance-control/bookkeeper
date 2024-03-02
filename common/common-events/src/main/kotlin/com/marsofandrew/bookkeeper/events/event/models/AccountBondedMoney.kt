package com.marsofandrew.bookkeeper.events.event.models

import com.marsofandrew.bookkeeper.properties.PositiveMoney

data class AccountBondedMoney(
    val money: PositiveMoney,
    val accountId: String?
)
