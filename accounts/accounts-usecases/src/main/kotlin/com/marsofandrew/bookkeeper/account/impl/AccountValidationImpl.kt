package com.marsofandrew.bookkeeper.account.impl

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.AccountValidation
import com.marsofandrew.bookkeeper.account.access.AccountStorage
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

class AccountValidationImpl(
    private val accountStorage: AccountStorage
) : AccountValidation {

    override fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean {
        return accountStorage.existsByUserIdAndAccountId(userId, accountId)
    }
}
