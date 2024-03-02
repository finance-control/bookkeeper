package com.marsofandrew.bookkeeper.events.event

import com.marsofandrew.bookkeeper.events.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import java.time.LocalDate

data class RollbackMoneyIsTransferredEvent(
    override val userId: Long,
    val date: LocalDate,
    val send: AccountBondedMoney?,
    val received: AccountBondedMoney,
    val category: Long,
) : UserEvent