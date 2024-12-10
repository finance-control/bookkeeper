package com.marsofandrew.bookkeeper.user

data class UserWithToken(
    val user: User,
    val token: String
)
