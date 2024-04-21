package com.marsofandrew.bookkeeper.transfers.impl.utils

import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.transfers.AccountMoney

fun AccountMoney.toAccountBoundedMoney() = AccountBondedMoney(
    money = money,
    accountId = accountId?.value
)