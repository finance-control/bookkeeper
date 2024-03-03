package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.AccountDeletion
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.Clock

class AccountDeletionImpl(
    private val accountStorage: AccountStorage,
    private val clock: Clock,
) : AccountDeletion {

    override fun delete(userId: NumericId<User>, ids: Set<StringId<Account>>) {
        if (ids.isEmpty()) return

        accountStorage.findAllByUserIdAndIds(userId, ids)
            .map { it.id }
            .toSet()
            .takeIf { it.isNotEmpty() }
            ?.let { accountStorage.setForRemovalAndClose(it, clock.date()) }
    }
}