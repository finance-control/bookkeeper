package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.AccountDeletion
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.Clock
import org.apache.logging.log4j.LogManager

//TODO: Refactor
class AccountDeletionImpl(
    private val accountStorage: AccountStorage,
    private val clock: Clock,
) : AccountDeletion {

    private val logger = LogManager.getLogger()

    override fun delete(userId: NumericId<User>, ids: Set<StringId<Account>>) {
        if (ids.isEmpty()) return
        val closedAt = clock.date()

        accountStorage.findAllByUserIdAndIds(userId, ids)
            .map { it.close(closedAt).id }
            .toSet()
            .takeIf { it.isNotEmpty() }
            ?.let {
                accountStorage.setForRemovalAndClose(it, closedAt)
                logger.info("Accounts: $it are closed")
            }
    }
}
