package com.marsofandrew.bookkeeper.accounts

import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface RollbackAccountMoneySpending {

    fun rollbackSpending(userId: NumericId<User>, from: AccountTransferAmount)
}