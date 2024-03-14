package com.marsofandrew.bookkeeper.user.fixture

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.UnregisteredUser
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials
import com.marsofandrew.bookkeeper.user.fixture.credentials.UserRawCredentialsFixture

fun user(id: NumericId<User>, init: UserFixture.() -> Unit = {}): User = UserFixture(id).apply(init).build()

fun unregisteredUser(init: UnregisteredUserFixture.() -> Unit = {}): UnregisteredUser =
    UnregisteredUserFixture().apply(init).build()

fun userRawCredentials(init: UserRawCredentialsFixture.() -> Unit = {}): UserRawCredentials =
    UserRawCredentialsFixture().apply(init).build()
