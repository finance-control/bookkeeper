package com.marsofandrew.bookkeeper.auth.provider

import com.marsofandrew.bookkeeper.tokens.TokenUserIdSelection
import org.springframework.stereotype.Service

@Service
internal class UserIdByTokenProviderImpl(
    private val userIdSelectionImpl: TokenUserIdSelection
) : UserIdByTokenProvider {

    override fun getIdByToken(token: String, ip: String, clientId: String): Long? {
        return userIdSelectionImpl.select(
            clientId = clientId,
            ipAddress = ip,
            token = token
        )?.value
    }
}
