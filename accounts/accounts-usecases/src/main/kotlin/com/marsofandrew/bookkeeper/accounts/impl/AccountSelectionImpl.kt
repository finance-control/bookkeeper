package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.AccountSelection
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage
import com.marsofandrew.bookkeeper.accounts.user.User
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