package com.marsofandrew.bookkeeper.tokens.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import com.marsofandrew.bookkeeper.tokens.TokenUserIdSelection
import com.marsofandrew.bookkeeper.tokens.access.TokenStorage
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Clock

class TokenUserIdSelectionImpl(
    private val tokenStorage: TokenStorage,
    private val clock: Clock
) : TokenUserIdSelection {

    override fun select(clientId: String, ipAddress: String?, token: String): NumericId<User>? {
        return tokenStorage.findAllByTokenAndClientIdNotExpired(token, clientId, clock.instant())
            .filter { filterByIpAddress(it, ipAddress) }
            .map { it.userId }
            .firstOrNull()
    }

    private fun filterByIpAddress(credentials: TokenCredentials, ipAddress: String?): Boolean {
        return credentials.ipAddress == null || ipAddress == credentials.ipAddress
    }
}
