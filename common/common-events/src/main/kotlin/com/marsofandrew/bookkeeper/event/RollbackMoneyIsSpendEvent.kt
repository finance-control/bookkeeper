package com.marsofandrew.bookkeeper.event

import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import java.time.LocalDate

data class RollbackMoneyIsSpendEvent(
    override val userId: Long,
    val date: LocalDate,
    val money: AccountBondedMoney,
    val category: Long,
) : UserEvent
