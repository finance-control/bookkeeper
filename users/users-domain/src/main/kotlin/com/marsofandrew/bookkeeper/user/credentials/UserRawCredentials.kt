package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.email.Email

data class UserRawCredentials(
    val email: Email,
    val password: String
) {
    init {
        validateFiled(password.isNotBlank()) { "password is blank" }
    }
}
