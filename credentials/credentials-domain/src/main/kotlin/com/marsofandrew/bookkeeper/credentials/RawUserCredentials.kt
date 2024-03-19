package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId

data class RawUserCredentials(
    val userId: NumericId<User>,
    val email: Email,
    val password: String,
) {
    init {
        validateFiled(password.isNotBlank()) { "password is blank" }
    }
}
