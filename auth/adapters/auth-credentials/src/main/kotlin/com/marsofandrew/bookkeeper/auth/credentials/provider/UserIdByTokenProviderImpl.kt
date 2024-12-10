package com.marsofandrew.bookkeeper.auth.credentials.provider

import com.marsofandrew.bookkeeper.auth.provider.UserIdByTokenProvider
import org.springframework.stereotype.Service

// TODO: move
@Service
internal class UserIdByTokenProviderImpl(): UserIdByTokenProvider {

    override fun getIdByToken(token: String, ip: String, clientId: String): Long? {
        TODO("Not yet implemented")
    }
}