package com.marsofandrew.bookkeeper.tokens.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.TokenExpiration
import com.marsofandrew.bookkeeper.tokens.access.TokenStorage
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Clock

class TokenExpirationImpl(
    private val tokenStorage: TokenStorage,
    private val clock: Clock,
) : TokenExpiration {

    override fun expire(userId: NumericId<User>, clientId: String, token: String) {
        val tokenCredentials = tokenStorage.findByUserIdAndClientIdAndTokenOrThrow(userId, clientId, token)

        tokenStorage.update(tokenCredentials.expire(clock.instant()))
    }

    override fun expireAll(userId: NumericId<User>) {
        val now = clock.instant()
        tokenStorage.findAllByUserId(userId).forEach {
            tokenStorage.update(it.expire(now))
        }
    }
}
