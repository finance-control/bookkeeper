package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.AccountSelection
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class AccountSelectionImpl(
    private val accountStorage: AccountStorage
) : AccountSelection {

    override fun select(userId: NumericId<User>): Set<Account> {
        return accountStorage.findAllByUserId(userId)
            .filter { it.status != Account.Status.FOR_REMOVAL }
            .toSet()
    }
}