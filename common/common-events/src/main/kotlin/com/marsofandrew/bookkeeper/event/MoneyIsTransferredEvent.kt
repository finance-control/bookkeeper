package com.marsofandrew.bookkeeper.event

import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import java.time.LocalDate

data class MoneyIsTransferredEvent(
    override val userId: Long,
    val date: LocalDate,
    val send: AccountBondedMoney?,
    val received: AccountBondedMoney,
    val category: Long,
) : UserEvent
