package com.marsofandrew.bookkeeper.account.fixtures

import com.marsofandrew.bookkeeper.account.Account
import com.marsofandrew.bookkeeper.account.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId

fun account(id: StringId<Account>, userId: NumericId<User>, init: AccountFixture.() -> Unit = {}): Account =
    AccountFixture(id, userId).apply(init).build()