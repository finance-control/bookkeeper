package com.marsofandrew.bookkeeper.account.events.listener

import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.properties.id.asId

internal fun AccountBondedMoney.toAccountTransferAmount(): AccountTransferAmount? {
    if (accountId == null) return null
    return AccountTransferAmount(
        accountId = accountId!!.asId(),
        money = money
    )
}
