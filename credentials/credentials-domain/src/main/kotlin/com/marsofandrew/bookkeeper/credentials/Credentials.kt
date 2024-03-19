package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.email.Email

data class Credentials(
    val email: Email,
    val password: String,
) {
    init {
        validateFiled(password.isNotBlank()) { "password is blank" }
    }
}
