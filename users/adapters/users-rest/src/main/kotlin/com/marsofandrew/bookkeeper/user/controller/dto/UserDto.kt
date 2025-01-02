package com.marsofandrew.bookkeeper.user.controller.dto

import com.marsofandrew.bookkeeper.user.UserWithEmail
import io.swagger.v3.oas.annotations.media.Schema

internal data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    @Schema(deprecated = true)
    @Deprecated(message = "Removed for security reasons")
    val surname: String = ""
)

internal fun UserWithEmail.toUserDto() =  UserDto(
    id = user.id.value,
    name = user.name,
    email = email.value
)
