package com.marsofandrew.bookkeeper.auth.security.client

import com.marsofandrew.bookkeeper.auth.client.ClientIdProvider
import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.controller.getRequestClientId
import org.springframework.stereotype.Service

@Service
internal class ClientIdProviderImpl : ClientIdProvider {

    override fun getClientId(): String = getRequestClientId() ?: throw IncorrectCredentialsException("Invalid client id")
}