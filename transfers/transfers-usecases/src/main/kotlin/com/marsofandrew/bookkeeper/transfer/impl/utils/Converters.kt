package com.marsofandrew.bookkeeper.transfer.impl.utils

import com.marsofandrew.bookkeeper.event.models.AccountBondedMoney
import com.marsofandrew.bookkeeper.transfer.AccountMoney

fun AccountMoney.toAccountBoundedMoney() = AccountBondedMoney(
    money = money,
    accountId = accountId?.value
)