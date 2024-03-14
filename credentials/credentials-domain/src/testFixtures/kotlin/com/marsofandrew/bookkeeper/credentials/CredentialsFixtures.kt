package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

fun credentials(init: CredentialsFixture.() -> Unit = {}): Credentials = CredentialsFixture().apply(init).build()

fun userCredentials(userId: NumericId<User>, init: UserCredentialsFixture.() -> Unit = {}): UserCredentials =
    UserCredentialsFixture(userId).apply(init).build()

fun rawUserCredentials(userId: NumericId<User>, init: RawUserCredentialsFixture.() -> Unit = {}): RawUserCredentials =
    RawUserCredentialsFixture(userId).apply(init).build()
