package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.AccountCleanup
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.base.date
import java.time.Clock
import java.time.Period
import org.apache.logging.log4j.LogManager

class AccountCleanupImpl(
    private val accountStorage: AccountStorage,
    private val clock: Clock,
    private val periodRemovalFilter: Period.() -> Boolean
) : AccountCleanup {

    private val logger = LogManager.getLogger()

    override fun clean(batchSize: Int) {
        logger.info("Start cleaning old accounts")
        do {
            val accountsForRemoval =
                accountStorage.findAccountsForRemoval(batchSize)
                    .filter {
                        it.closedAt != null
                                && Period.between(it.closedAt, clock.date()).periodRemovalFilter()
                                && it.money.isZero()
                    }
            accountStorage.delete(accountsForRemoval.mapTo(HashSet()) { it.id })
            logger.info("Old accounts: ${accountsForRemoval.map { it.id }} are removed")
        } while (accountsForRemoval.isNotEmpty())
        logger.info("All old accounts are removed")
    }
}
