package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

interface AccountDeletion {

    fun delete(userId: NumericId<User>, ids: Set<StringId<Account>>)
}