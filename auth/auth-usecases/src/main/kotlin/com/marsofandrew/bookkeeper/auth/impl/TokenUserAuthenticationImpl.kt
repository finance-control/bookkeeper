package com.marsofandrew.bookkeeper.auth.impl

import com.marsofandrew.bookkeeper.auth.client.ClientIdProvider
import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.auth.ip.IpAddressProvider
import com.marsofandrew.bookkeeper.auth.provider.UserIdByTokenProvider

class TokenUserAuthenticationImpl(
    private val userIdByTokenProvider: UserIdByTokenProvider,
    private val ipAddressProvider: IpAddressProvider,
    private val clientIdProvider: ClientIdProvider
) : AbstractUserAuthenticationImpl() {

    override fun getUserIdByAuth(authKey: String): Long {
        val clientId = clientIdProvider.getClientId()

        return userIdByTokenProvider.getIdByToken(authKey, ipAddressProvider.getIpAddress(), clientId)
            ?: throw IncorrectCredentialsException("Incorrect token")
    }
}