package com.marsofandrew.bookkeeper.auth.security.client

import com.marsofandrew.bookkeeper.auth.client.ClientIdProvider
import com.marsofandrew.bookkeeper.userContext.getRequestClientId
import org.springframework.stereotype.Service

@Service
internal class ClientIdProviderImpl : ClientIdProvider {

    override fun getClientId(): String? = getRequestClientId()
}