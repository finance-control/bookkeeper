package com.marsofandrew.bookkeeper.user.token

import java.time.Instant

data class UserToken(
    val token: String,
    val expiredAt: Instant
)
