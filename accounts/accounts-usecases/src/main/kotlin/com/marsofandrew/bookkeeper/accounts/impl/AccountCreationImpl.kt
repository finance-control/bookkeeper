package com.marsofandrew.bookkeeper.accounts.impl

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.AccountCreation
import com.marsofandrew.bookkeeper.accounts.access.AccountStorage

class AccountCreationImpl(
    private val accountStorage: AccountStorage
): AccountCreation {

    override fun create(account: Account): Account {
        return accountStorage.create(account)
    }
}