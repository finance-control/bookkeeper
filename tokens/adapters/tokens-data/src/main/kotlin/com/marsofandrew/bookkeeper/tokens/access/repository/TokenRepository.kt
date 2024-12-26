package com.marsofandrew.bookkeeper.tokens.access.repository

import com.marsofandrew.bookkeeper.tokens.access.entity.TokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
internal interface TokenRepository : JpaRepository<TokenEntity, TokenEntity.TokenId> {

    @Query(
        "SELECT * from bookkeeper.app_tokens where user_id = :userId",
        nativeQuery = true
    )
    fun findAllByUserId(userId: Long): List<TokenEntity>

    @Query(
        """
        SELECT * FROM bookkeeper.app_tokens
        WHERE client_id = :clientId and token = :token and expired_at > :now
    """,
        nativeQuery = true
    )
    fun findAllByClientIdAndTokenAndExpiredAfter(clientId: String, token: String, now: Instant): List<TokenEntity>

    @Query(
        """
        SELECT * FROM bookkeeper.app_tokens
        WHERE user_id = :userId and client_id = :clientId and expired_at > :now
    """,
        nativeQuery = true
    )
    fun findAllByUserIdAndClientIdAndExpiredAfter(userId: Long, clientId: String, now: Instant): List<TokenEntity>


    @Modifying
    @Query(
        """
            UPDATE TokenEntity SET expiredAt = :now WHERE tokenId = :tokenId
        """
    )
    fun expireById(tokenId: TokenEntity.TokenId, now: Instant)
}
