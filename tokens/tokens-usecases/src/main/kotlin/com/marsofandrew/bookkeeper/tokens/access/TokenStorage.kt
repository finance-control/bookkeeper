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

    //TODO: remove
    fun findAllByTokenAndClientIdNotExpired(token: String, clientId: String, now: Instant): List<TokenCredentials>

    fun findAllByUserIdANdClientIdNotExpired(userId: NumericId<User>, clientId: String, now: Instant): List<TokenCredentials>

    fun create(tokenCredentials: TokenCredentials): TokenCredentials
    fun update(tokenCredentials: TokenCredentials): TokenCredentials
    fun deleteExpiredBefore(now: Instant)
}