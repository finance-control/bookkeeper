package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.AccountMoneySpending
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
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