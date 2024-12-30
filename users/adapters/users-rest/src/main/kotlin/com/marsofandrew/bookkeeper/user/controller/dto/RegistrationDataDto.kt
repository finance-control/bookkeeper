package com.marsofandrew.bookkeeper.user.controller.dto

import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.user.UnregisteredUser
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials

internal data class RegistrationDataDto(
    val name: String,
    val email: String,
    val password: String
)

internal fun RegistrationDataDto.toUnregisteredUser() = UnregisteredUser(
    name = name,
    rawCredentials = UserRawCredentials(
        email = Email(email),
        password = password
    )
)
