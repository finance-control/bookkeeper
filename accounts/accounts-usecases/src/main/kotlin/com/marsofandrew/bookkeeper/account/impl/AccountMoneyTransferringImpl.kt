package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.AccountMoneyTransferring
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.transfer.AccountTransferAmount
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId

class AccountMoneyTransferringImpl(
    private val accountStorage: AccountStorage,
    private val transactionalExecution: TransactionalExecution
) : AccountMoneyTransferring {

    override fun transfer(userId: NumericId<User>, from: AccountTransferAmount?, to: AccountTransferAmount?) {
        transactionalExecution.execute {
            val toAccount = to?.let { accountStorage.findByUserIdAndIdOrThrow(userId, to.accountId).topUp(to.money) }
            val fromAccount =
                from?.let { accountStorage.findByUserIdAndIdOrThrow(userId, from.accountId).withdraw(from.money) }

            listOf(toAccount, fromAccount)
                .forEach { account ->
                    account?.let {
                        accountStorage.setMoney(it.id, it.money)
                    }
                }
        }
    }
}