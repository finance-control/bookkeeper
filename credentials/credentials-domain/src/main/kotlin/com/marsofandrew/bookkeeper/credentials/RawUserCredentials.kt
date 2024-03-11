package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.Instant

data class RawUserCredentials(
    val userId: NumericId<User>,
    val email: Email,
    val password: String,
) {
    init {
        check(password.isNotBlank()) { "password is blank" }
        check(password.length >= 8) { "Password has length less than 8" }
    }
}