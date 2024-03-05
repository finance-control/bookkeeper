package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.AccountCleanup
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.base.date
import java.time.Clock
import java.time.Period

class AccountCleanupImpl(
    private val accountStorage: AccountStorage,
    private val clock: Clock,
    private val periodRemovalFilter: Period.() -> Boolean
) : AccountCleanup {

    override fun clean(batchSize: Int) {
        do {
            val accountsForRemoval =
                accountStorage.findAccountsForRemoval(batchSize)
                    .filter { it.closedAt != null && Period.between(it.closedAt, clock.date()).periodRemovalFilter() }
            accountStorage.delete(accountsForRemoval.mapTo(HashSet()) { it.id })
        } while (accountsForRemoval.isNotEmpty())
    }
}