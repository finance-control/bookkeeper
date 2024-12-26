package com.marsofandrew.bookkeeper.tokens.impl

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.TokenCreation
import com.marsofandrew.bookkeeper.tokens.TokenCreationParams
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import com.marsofandrew.bookkeeper.tokens.access.TokenStorage
import com.marsofandrew.bookkeeper.tokens.hash.HashGenerator
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Clock
import java.util.*

class TokenCreationImpl(
    private val tokenStorage: TokenStorage,
    private val hashGenerator: HashGenerator,
    private val clock: Clock
) : TokenCreation {

    override fun create(userId: NumericId<User>, creationParams: TokenCreationParams): TokenCredentials {
        val now = clock.instant()
        val expiredAt = now + creationParams.ttl

        val rawToken = TokenCredentials(
            userId = userId,
            clientId = creationParams.clientId,
            ipAddress = creationParams.ipAddress,
            createdAt = now,
            expiredAt = expiredAt,
            token = createRawToken(userId, creationParams),
            version = Version(0)
        )

        tokenStorage.create(rawToken)

        return rawToken
    }

    private fun createRawToken(userId: NumericId<User>, creationParams: TokenCreationParams): String {
        val token = "UserId: ${userId.value}, " +
                "ClientId: ${creationParams.clientId}, " +
                "IP: ${creationParams.ipAddress}, " +
                "ttl: ${creationParams.ttl}" +
                "UUID: ${UUID.randomUUID()}"
        return Base64.getEncoder().encodeToString(hashGenerator.hash(token.toByteArray()))
    }

}