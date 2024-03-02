package com.marsofandrew.bookkeeper.accounts.events.listener

import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.events.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.id.asId

internal fun AccountBondedMoney.toAccountTransferAmount(): AccountTransferAmount? {
    if (accountId == null) return null
    return AccountTransferAmount(
        accountId = accountId!!.asId(),
        money = money
    )
}