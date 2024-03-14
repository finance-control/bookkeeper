package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class RawUserCredentialsFixture(
    val userId: NumericId<User>
) {

    var email: Email = Email("test@test.com")
    var password: String = "password"

    fun build() = RawUserCredentials(
        userId = userId,
        email = email,
        password = password
    )
}