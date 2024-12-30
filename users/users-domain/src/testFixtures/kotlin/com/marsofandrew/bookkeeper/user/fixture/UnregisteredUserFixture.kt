package com.marsofandrew.bookkeeper.user.fixture

import com.marsofandrew.bookkeeper.user.UnregisteredUser
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials
import com.marsofandrew.bookkeeper.user.fixture.credentials.UserRawCredentialsFixture

class UnregisteredUserFixture {

    var name: String = "name"
    var rawCredentials: UserRawCredentials = UserRawCredentialsFixture().build()

    fun build() = UnregisteredUser(
        name = name,
        rawCredentials = rawCredentials
    )
}
