package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.email.Email

data class Credentials(
    val email: Email,
    val password: String,
) {
    init {
        check(password.isNotBlank()) { "password is blank" }
    }
}
