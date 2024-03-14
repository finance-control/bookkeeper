package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.Instant

data class UserCredentialsFixture(val userId: NumericId<User>) {

    var email: Email = Email("test@test.com")
    var password: String = "password"
    var updatedAt: Instant = Instant.now()
    var version: Version = Version(0)

    fun build() = UserCredentials(
        userId = userId,
        email = email,
        password = password,
        updatedAt = updatedAt,
        version = version
    )
}
