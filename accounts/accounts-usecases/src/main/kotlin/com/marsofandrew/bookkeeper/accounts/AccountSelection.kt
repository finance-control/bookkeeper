package com.marsofandrew.bookkeeper.accounts

import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface AccountSelection {

    fun select(userId: NumericId<User>): Set<Account>
}