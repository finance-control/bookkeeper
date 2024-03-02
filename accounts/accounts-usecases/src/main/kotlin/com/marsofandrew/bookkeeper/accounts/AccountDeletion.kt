package com.marsofandrew.bookkeeper.accounts

import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

interface AccountDeletion {

    fun delete(userId: NumericId<User>, ids: Set<StringId<Account>>)
}