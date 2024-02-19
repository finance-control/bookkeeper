package com.marsofandrew.bookkeeper.auth.impl

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.exception.forbidden

abstract class AbstractUserAuthenticationImpl : UserAuthentication {

    override fun authenticate(authKey: String): Long {
        return runCatching { getUserIdByAuth(authKey) }
            .onFailure { e -> forbidden(e) }
            .getOrThrow()
    }

    abstract fun getUserIdByAuth(authKey: String): Long
}