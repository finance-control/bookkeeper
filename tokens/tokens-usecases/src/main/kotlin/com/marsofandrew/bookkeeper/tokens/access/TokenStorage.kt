package com.marsofandrew.bookkeeper.tokens.access

import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Instant

interface TokenStorage {

    fun findByUserIdAndClientIdAndTokenOrThrow(
        userId: NumericId<User>,
        clientId: String,
        token: String
    ): TokenCredentials =
        findByUserIdAndClientIdAndToken(userId, clientId, token).orElseThrow(token.asId<TokenCredentials>())

    fun findByUserIdAndClientIdAndToken(userId: NumericId<User>, clientId: String, token: String): TokenCredentials?
    fun findAllByUserId(userId: NumericId<User>): List<TokenCredentials>

    fun findAllByTokenAndClientIdNotExpired(token: String, clientId: String, now: Instant): List<TokenCredentials>

    fun create(tokenCredentials: TokenCredentials): TokenCredentials
    fun expire(userId: NumericId<User>, clientId: String, token: String, now: Instant)
    fun deleteExpiredBefore(now: Instant)
}