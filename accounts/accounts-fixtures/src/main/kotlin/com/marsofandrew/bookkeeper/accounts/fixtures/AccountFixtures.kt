package com.marsofandrew.bookkeeper.accounts.fixtures

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

fun account(id: StringId<Account>, userId: NumericId<User>, init: AccountFixture.() -> Unit = {}): Account =
    AccountFixture(id, userId).apply(init).build()