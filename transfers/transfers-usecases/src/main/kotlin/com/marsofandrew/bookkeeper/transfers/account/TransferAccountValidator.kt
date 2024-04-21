package com.marsofandrew.bookkeeper.transfers.account

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.user.User

interface TransferAccountValidator {

    fun validate(userId: NumericId<User>, accountId: StringId<Account>): Boolean
}