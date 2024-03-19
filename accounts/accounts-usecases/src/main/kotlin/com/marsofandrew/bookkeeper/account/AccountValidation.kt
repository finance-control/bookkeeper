package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

interface AccountValidation {

    fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean
}
