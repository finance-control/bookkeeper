package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.user.User

interface TokenExpiration {

    fun expire(userId: NumericId<User>, clientId: String, token: String)

    fun expireAll(userId: NumericId<User>)
}