package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface AccountMoneySpending {

    fun spend(userId: NumericId<User>, from: AccountTransferAmount)
}
