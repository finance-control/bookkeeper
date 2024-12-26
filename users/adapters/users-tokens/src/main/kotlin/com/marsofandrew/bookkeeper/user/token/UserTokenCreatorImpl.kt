package com.marsofandrew.bookkeeper.user.token

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.tokens.TokenCreation
import com.marsofandrew.bookkeeper.tokens.TokenCreationParams
import com.marsofandrew.bookkeeper.user.User
import java.time.Clock
import java.time.Duration
import org.springframework.stereotype.Service

@Service
internal class UserTokenCreatorImpl(
    private val tokenCreation: TokenCreation,
    private val clock: Clock
) : UserTokenCreator {

    override fun getOrCreate(userId: NumericId<User>, clientId: String, ipAddress: String?, ttl: Duration): UserToken {
        val createdToken = tokenCreation.create(
            userId = userId.value.asId(),
            creationParams = TokenCreationParams(
                clientId = clientId,
                ipAddress = ipAddress,
                ttl = ttl
            )
        )

        return UserToken(token = createdToken.token, expiredAt = createdToken.expiredAt)
    }
}