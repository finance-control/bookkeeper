package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.email.Email

data class UserWithEmail(
    val user: User,
    val email: Email
)
