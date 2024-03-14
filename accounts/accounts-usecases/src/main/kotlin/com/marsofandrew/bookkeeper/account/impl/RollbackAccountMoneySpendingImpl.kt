package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.RollbackAccountMoneySpending
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.id.NumericId

class RollbackAccountMoneySpendingImpl(
    private val accountStorage: AccountStorage,
    private val transactionExecutor: TransactionExecutor
): RollbackAccountMoneySpending {
    override fun rollbackSpending(userId: NumericId<User>, from: AccountTransferAmount) {
        transactionExecutor.execute {
            val account = accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).topUp(from.money)
            accountStorage.setMoney(account.id, account.money)
        }
    }
}