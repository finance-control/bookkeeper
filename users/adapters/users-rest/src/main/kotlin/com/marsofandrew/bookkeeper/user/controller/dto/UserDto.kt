package com.marsofandrew.bookkeeper.user.controller.dto

import com.marsofandrew.bookkeeper.user.UserWithEmail

internal data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    //Schema(deprecated = true)
    val surname: String = "",
)

internal fun UserWithEmail.toUserDto() =  UserDto(
    id = user.id.value,
    name = user.name,
    email = email.value
)
