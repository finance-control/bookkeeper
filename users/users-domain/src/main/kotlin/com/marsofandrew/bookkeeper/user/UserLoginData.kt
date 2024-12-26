package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.id.NumericId

data class UserLoginData(
    val id: NumericId<User>,
    val token: String
)
