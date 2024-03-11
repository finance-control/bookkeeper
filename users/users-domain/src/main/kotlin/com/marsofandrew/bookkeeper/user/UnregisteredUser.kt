package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials

data class UnregisteredUser(
    val name: String,
    val surname: String,
    val rawCredentials: UserRawCredentials
) {
    init {
        check(name.isNotBlank()) { "name is blank" }
        check(surname.isNotBlank()) { "surname is blank" }
    }
}
