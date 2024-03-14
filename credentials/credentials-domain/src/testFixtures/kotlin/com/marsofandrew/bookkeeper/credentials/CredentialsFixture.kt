package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.email.Email

class CredentialsFixture {

    var email: Email = Email("test@test.com")
    var password: String = "password"

    fun build() = Credentials(
        email = email,
        password = password
    )
}