package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface AccountMoneyTransferring {

    fun transfer(userId: NumericId<User>, source: AccountTransferAmount?, destination: AccountTransferAmount?)
}
