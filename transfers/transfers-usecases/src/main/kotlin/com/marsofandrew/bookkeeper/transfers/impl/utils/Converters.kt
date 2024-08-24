package com.marsofandrew.bookkeeper.transfers.impl.utils

import com.marsofandrew.bookkeeper.event.MoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.RollbackMoneyIsTransferredEvent
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase

fun AccountMoney.toAccountBoundedMoney() = AccountBondedMoney(
    money = money,
    accountId = accountId?.value
)

internal fun CommonTransferBase.toMoneyIsTransferredEvent() = MoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = categoryId.value
)

internal fun CommonTransferBase.toRollbackMoneyIsTransferredEvent() = RollbackMoneyIsTransferredEvent(
    userId = userId.value,
    date = date,
    send = send?.toAccountBoundedMoney(),
    received = received.toAccountBoundedMoney(),
    category = categoryId.value
)