package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.RollbackAccountMoneySpending
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId

class RollbackAccountMoneySpendingImpl(
    private val accountStorage: AccountStorage,
    private val transactionalExecution: TransactionalExecution
): RollbackAccountMoneySpending {
    override fun rollbackSpending(userId: NumericId<User>, from: AccountTransferAmount) {
        transactionalExecution.execute {
            val account = accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).topUp(from.money)
            accountStorage.setMoney(account.id, account.money)
        }
    }
}