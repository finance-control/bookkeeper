package com.marsofandrew.bookkeeper.spending.account

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spending.user.User

interface AccountValidator {

    fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean
}