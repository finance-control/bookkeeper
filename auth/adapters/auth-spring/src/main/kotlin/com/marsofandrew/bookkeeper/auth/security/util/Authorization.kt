package com.marsofandrew.bookkeeper.auth.security.util

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.exception.ForbiddenException
import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.auth.security.exception.ForbiddenAuthException
import com.marsofandrew.bookkeeper.auth.security.exception.InternalAuthError
import org.springframework.security.authentication.InsufficientAuthenticationException

fun UserAuthentication.authenticateOrThrow(authKey: String): Long = runCatching {
    authenticate(authKey)
}.onFailure { e ->
    when (e) {
        is ForbiddenException -> throw ForbiddenAuthException(e)
        is IncorrectCredentialsException -> throw InsufficientAuthenticationException("Invalid credentials", e)
        else -> throw InternalAuthError(e)
    }
}.getOrThrow()
