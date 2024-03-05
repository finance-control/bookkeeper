package com.marsofandrew.bookkeeper.account

import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface AccountSelection {

    fun select(userId: NumericId<User>): Set<Account>
}