package com.marsofandrew.bookkeeper.tokens.impl

import com.marsofandrew.bookkeeper.tokens.ExpiredTokenDeletion
import com.marsofandrew.bookkeeper.tokens.access.TokenStorage
import java.time.Clock
import java.time.Duration

class ExpiredTokenDeletionImpl(
    private val tokenStorage: TokenStorage,
    private val clock: Clock
) : ExpiredTokenDeletion {

    private val SAFE_DURATION = Duration.ofHours(1)

    override fun delete() {
        val now = clock.instant() - SAFE_DURATION

        tokenStorage.deleteExpiredBefore(now)
    }
}