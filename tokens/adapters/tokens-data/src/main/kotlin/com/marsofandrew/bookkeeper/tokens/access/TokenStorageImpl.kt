package com.marsofandrew.bookkeeper.tokens.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import com.marsofandrew.bookkeeper.tokens.access.entity.TokenEntity
import com.marsofandrew.bookkeeper.tokens.access.entity.toTokenEntity
import com.marsofandrew.bookkeeper.tokens.access.repository.TokenRepository
import com.marsofandrew.bookkeeper.tokens.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
internal class TokenStorageImpl(
    private val tokenRepository: TokenRepository
) : TokenStorage {

    override fun findByUserIdAndClientIdAndToken(
        userId: NumericId<User>,
        clientId: String,
        token: String
    ): TokenCredentials? {
        return tokenRepository.findById(
            TokenEntity.TokenId(
                clientId = clientId,
                userId = userId.value,
                token = token
            )
        ).map { it.toModel() }
            .orElse(null)
    }

    override fun findAllByUserId(userId: NumericId<User>): List<TokenCredentials> {
        return tokenRepository.findAllByUserId(userId.value).toModels()
    }

    override fun findAllByTokenAndClientIdNotExpired(
        token: String,
        clientId: String,
        now: Instant
    ): List<TokenCredentials> {
        return tokenRepository.findAllByClientIdAndTokenAndExpiredAfter(clientId, token, now).toModels()
    }

    override fun create(tokenCredentials: TokenCredentials): TokenCredentials {
        return tokenRepository.save(tokenCredentials.toTokenEntity()).toModel()
    }

    @Transactional
    override fun expire(userId: NumericId<User>, clientId: String, token: String, now: Instant) {
        tokenRepository.expireById(TokenEntity.TokenId(userId = userId.value, clientId = clientId, token = token), now)
    }

    @Transactional
    override fun deleteExpiredBefore(now: Instant) {
        TODO()
    }


}