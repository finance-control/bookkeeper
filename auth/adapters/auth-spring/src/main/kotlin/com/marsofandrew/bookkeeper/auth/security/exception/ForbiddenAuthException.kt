package com.marsofandrew.bookkeeper.auth.security.exception

import com.marsofandrew.bookkeeper.auth.exception.ForbiddenException
import org.springframework.security.core.AuthenticationException

internal class ForbiddenAuthException(
    forbiddenException: ForbiddenException
) : AuthenticationException(forbiddenException.message, forbiddenException)