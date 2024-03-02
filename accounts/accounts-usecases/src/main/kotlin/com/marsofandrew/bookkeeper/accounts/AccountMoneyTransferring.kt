package com.marsofandrew.bookkeeper.accounts

import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface AccountMoneyTransferring {

    fun transfer(userId: NumericId<User>, from: AccountTransferAmount?, to: AccountTransferAmount?)
}