package com.marsofandrew.bookkeeper.auth.security.exception

import org.springframework.security.core.AuthenticationException

internal class InternalAuthError(
    exception: Throwable
) : AuthenticationException(exception.message, exception)