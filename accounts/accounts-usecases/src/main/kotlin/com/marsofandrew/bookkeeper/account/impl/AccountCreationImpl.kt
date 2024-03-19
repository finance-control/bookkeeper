package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.AccountCreation
import com.marsofandrew.bookkeeper.account.access.AccountStorage

class AccountCreationImpl(
    private val accountStorage: AccountStorage
) : AccountCreation {

    override fun create(account: Account): Account {
        return accountStorage.create(account)
    }
}
