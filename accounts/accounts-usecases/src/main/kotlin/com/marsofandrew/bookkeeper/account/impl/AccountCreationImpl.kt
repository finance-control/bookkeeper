package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.AccountCreation
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import org.apache.logging.log4j.LogManager

class AccountCreationImpl(
    private val accountStorage: AccountStorage
) : AccountCreation {

    private val logger = LogManager.getLogger()

    override fun create(account: Account): Account {
        return accountStorage.create(account)
            .also {
                logger.info("account ${it.id} is created")
            }
    }
}
