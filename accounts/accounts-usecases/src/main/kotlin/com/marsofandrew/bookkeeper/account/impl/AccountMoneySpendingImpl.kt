package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.AccountMoneySpending
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId

class AccountMoneySpendingImpl(
    private val accountStorage: AccountStorage,
    private val transactionalExecution: TransactionalExecution
) : AccountMoneySpending {

    override fun spend(userId: NumericId<User>, from: AccountTransferAmount) {
        transactionalExecution.execute {
            val account = accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).withdraw(from.money)
            accountStorage.setMoney(account.id, account.money)
        }
    }
}