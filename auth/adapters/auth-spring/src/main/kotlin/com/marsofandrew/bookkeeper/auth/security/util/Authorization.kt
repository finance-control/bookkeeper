package com.marsofandrew.bookkeeper.auth.security.util

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.exception.ForbiddenException
import com.marsofandrew.bookkeeper.auth.security.exception.ForbiddenAuthException
import com.marsofandrew.bookkeeper.auth.security.exception.InternalAuthError

fun UserAuthentication.authenticateOrThrow(authKey: String): Long = runCatching {
    authenticate(authKey)
}.onFailure { e ->
    when (e) {
        is ForbiddenException -> ForbiddenAuthException(e)
        else -> InternalAuthError(e)
    }
}.getOrThrow()