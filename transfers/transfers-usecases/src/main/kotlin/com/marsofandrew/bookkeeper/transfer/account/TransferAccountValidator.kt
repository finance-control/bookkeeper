package com.marsofandrew.bookkeeper.transfer.account

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfer.user.User

interface TransferAccountValidator {

    fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean
}