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

    override fun rollbackTransfer(userId: NumericId<User>, from: AccountTransferAmount?, to: AccountTransferAmount?) {
        transactionExecutor.execute {
            val toAccount = to?.let { accountStorage.findByUserIdAndIdOrThrow(userId, to.accountId).withdraw(to.money) }
            val fromAccount =
                from?.let { accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).topUp(from.money) }

            listOf(toAccount, fromAccount)
                .forEach { account ->
                    account?.let {
                        accountStorage.setMoney(it.id, it.money)
                    }
                }
        }
    }
}
