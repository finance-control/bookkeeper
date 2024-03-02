package com.marsofandrew.bookkeeper.events.event

import com.marsofandrew.bookkeeper.events.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import java.time.LocalDate

data class MoneyIsSpendEvent(
    override val userId: Long,
    val date: LocalDate,
    val money: AccountBondedMoney,
    val category: Long,
): UserEvent
