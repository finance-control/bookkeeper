package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.RollbackAccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.id.NumericId

class RollbackAccountMoneyTransferringImpl(
    private val accountStorage: AccountStorage,
    private val transactionExecutor: TransactionExecutor
) : RollbackAccountMoneyTransferring {

    override fun rollbackTransfer(
        userId: NumericId<User>,
        source: AccountTransferAmount?,
        destination: AccountTransferAmount?
    ) {
        transactionExecutor.execute {
            val destinationAccount = destination?.let {
                accountStorage.findByUserIdAndIdOrThrow(userId, destination.accountId).withdraw(destination.money)
            }
            val sourceAccount =
                source?.let { accountStorage.findByUserIdAndIdOrThrow(userId, source.accountId).topUp(source.money) }

            listOf(destinationAccount, sourceAccount)
                .forEach { account ->
                    account?.let {
                        accountStorage.setMoney(it.id, it.money)
                    }
                }
        }
    }
}
