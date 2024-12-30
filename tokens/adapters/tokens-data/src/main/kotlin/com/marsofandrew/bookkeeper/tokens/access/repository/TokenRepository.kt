package com.marsofandrew.bookkeeper.tokens.access.repository

import com.marsofandrew.bookkeeper.tokens.access.entity.TokenEntity
import java.time.Instant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface TokenRepository : JpaRepository<TokenEntity, Long> {

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

}
