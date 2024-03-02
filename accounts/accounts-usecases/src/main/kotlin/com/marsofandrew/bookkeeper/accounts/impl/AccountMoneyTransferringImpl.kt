package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.AccountMoneyTransferring
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId

class AccountMoneyTransferringImpl(
    private val accountStorage: AccountStorage,
    private val transactionalExecution: TransactionalExecution
) : AccountMoneyTransferring {

    override fun transfer(userId: NumericId<User>, from: AccountTransferAmount?, to: AccountTransferAmount?) {
        transactionalExecution.execute {
            if (to != null) {
                val toAccount = accountStorage.findByUserIdAndIdOrThrow(userId, to.accountId).topUp(to.money)
                accountStorage.setMoney(toAccount.id, toAccount.money)
            }
            if (from != null) {
                val fromAccount = accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).withdraw(from.money)
                accountStorage.setMoney(fromAccount.id, fromAccount.money)
            }
        }
    }
}