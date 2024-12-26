package com.marsofandrew.bookkeeper.auth.security.client

import com.marsofandrew.bookkeeper.auth.client.ClientIdProvider
import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.userContext.getRequestClientId
import org.springframework.stereotype.Service

@Service
internal class ClientIdProviderImpl : ClientIdProvider {

    // TODO: check
    override fun getClientId(): String = getRequestClientId() ?: "ddd"//throw IncorrectCredentialsException("Invalid client id")
}