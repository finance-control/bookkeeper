package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.user.User

interface TokenCreation {

    fun create(userId: NumericId<User>, creationParams: TokenCreationParams): TokenCredentials
}
