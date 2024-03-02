package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.RollbackAccountMoneyTransferring
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId

class RollbackAccountMoneyTransferringImpl(
    private val accountStorage: AccountStorage,
    private val transactionalExecution: TransactionalExecution
) : RollbackAccountMoneyTransferring {

    override fun rollbackTransfer(userId: NumericId<User>, from: AccountTransferAmount?, to: AccountTransferAmount?) {
        transactionalExecution.execute {
            if (to != null) {
                val toAccount = accountStorage.findByUserIdAndIdOrThrow(userId, to.accountId).withdraw(to.money)
                accountStorage.setMoney(toAccount.id, toAccount.money)
            }
            if (from != null) {
                val fromAccount = accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).topUp(from.money)
                accountStorage.setMoney(fromAccount.id, fromAccount.money)
            }
        }
    }
}