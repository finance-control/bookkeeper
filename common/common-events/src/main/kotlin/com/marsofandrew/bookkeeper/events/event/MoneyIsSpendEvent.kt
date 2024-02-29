package com.marsofandrew.bookkeeper.events.event

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import java.time.LocalDate

data class MoneyIsSpendEvent(
    override val userId: Long,
    val date: LocalDate,
    val money: PositiveMoney
): UserEvent
