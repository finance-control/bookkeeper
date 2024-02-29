package com.marsofandrew.bookkeeper.events.event

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import java.time.LocalDate

data class MoneyIsTransferredEvent(
    override val userId: Long,
    val date: LocalDate,
    val send: PositiveMoney?,
    val received: PositiveMoney
) : UserEvent