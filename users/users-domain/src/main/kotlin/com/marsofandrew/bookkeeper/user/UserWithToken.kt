package com.marsofandrew.bookkeeper.user

import java.time.Instant

data class UserWithToken(
    val user: User,
    val token: String,
    val tokenExpiresAt: Instant
)
