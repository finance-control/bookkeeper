package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials

data class UnregisteredUser(
    val name: String,
    val rawCredentials: UserRawCredentials
) {
    init {
        validateFiled(name.isNotBlank()) { "name is blank" }
    }
}
