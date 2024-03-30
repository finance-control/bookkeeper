package com.marsofandrew.bookkeeper.user.controller.dto

import com.marsofandrew.bookkeeper.user.User

internal data class UserDto(
    val id: Long,
    val name: String,
    val surname: String,
)

internal fun User.toUserDto() =  UserDto(
    id = id.value,
    name = name,
    surname = surname
)
