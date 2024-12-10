package com.marsofandrew.bookkeeper.auth.security.provider

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.security.authentication.TokenAuthentication
import com.marsofandrew.bookkeeper.auth.security.util.authenticateOrThrow
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

internal class TokenAuthenticationProvider(
    private val userAuthentication: UserAuthentication
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val auth = authentication as TokenAuthentication
        val userId = userAuthentication.authenticateOrThrow(auth.token)

        return UserIdToken(userId)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.isAssignableFrom(TokenAuthentication::class.java) ?: false
    }
}
