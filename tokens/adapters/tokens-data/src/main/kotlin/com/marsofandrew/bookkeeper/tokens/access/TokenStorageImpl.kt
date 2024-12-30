package com.marsofandrew.bookkeeper.tokens.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import com.marsofandrew.bookkeeper.tokens.access.entity.TokenEntity
import com.marsofandrew.bookkeeper.tokens.access.entity.toTokenEntity
import com.marsofandrew.bookkeeper.tokens.access.repository.TokenRepository
import com.marsofandrew.bookkeeper.tokens.encryption.TokenEncryptor
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Instant
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class TokenStorageImpl(
    private val tokenRepository: TokenRepository,
    private val tokenEncryptor: TokenEncryptor,
) : TokenStorage {

    override fun findByUserIdAndClientIdAndToken(
        userId: NumericId<User>,
        clientId: String,
        token: String
    ): TokenCredentials? {
        TODO("findByUserIdAndClientIdAndToken have not been implemented yet")
    }

    override fun findAllByUserId(userId: NumericId<User>): List<TokenCredentials> {
        return tokenRepository.findAllByUserId(userId.value)
            .map { it.toDecryptedModel() }
    }

    override fun findAllByTokenAndClientIdNotExpired(
        token: String,
        clientId: String,
        now: Instant
    ): List<TokenCredentials> {
        return tokenRepository.findAllByClientIdAndTokenAndExpiredAfter(clientId, tokenEncryptor.encrypt(token), now)
            .map { it.toDecryptedModel() }
    }

    override fun findAllByUserIdANdClientIdNotExpired(
        userId: NumericId<User>,
        clientId: String,
        now: Instant
    ): List<TokenCredentials> {
        return tokenRepository.findAllByUserIdAndClientIdAndExpiredAfter(userId.value, clientId, now)
            .map { it.toDecryptedModel() }
    }

    @Transactional
    override fun create(tokenCredentials: TokenCredentials): TokenCredentials {
        return tokenRepository.save(tokenCredentials.toEncryptedEntity()).toDecryptedModel()
    }

    @Transactional
    override fun update(tokenCredentials: TokenCredentials): TokenCredentials {
        return tokenRepository.save(tokenCredentials.toEncryptedEntity()).toDecryptedModel()
    }

    @Transactional
    override fun deleteExpiredBefore(now: Instant) {
        TODO()
    }

    private fun TokenCredentials.toEncryptedEntity(): TokenEntity =
        this.toTokenEntity().copy(token = tokenEncryptor.encrypt(token))

    private fun TokenEntity.toDecryptedModel(): TokenCredentials =
        this.toModel().copy(token = tokenEncryptor.decrypt(token))
}
