package com.marsofandrew.bookkeeper.user.fixture.credentials

import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials

class UserRawCredentialsFixture {

    var email: Email = Email("test@test.com")
    var password: String = "password"

    fun build() = UserRawCredentials(
        email = email,
        password = password
    )
}