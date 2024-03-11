package com.marsofandrew.bookkeeper.auth.security.provider

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.security.authentication.BasicAuthentication
import com.marsofandrew.bookkeeper.auth.security.authentication.FakeAuthentication
import com.marsofandrew.bookkeeper.auth.security.util.authenticateOrThrow
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

internal class BasicAuthenticationProvider(
    private val userAuthentication: UserAuthentication
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val auth = authentication as BasicAuthentication
        val userId = userAuthentication.authenticateOrThrow(auth.credentials)

        return UserIdToken(userId)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.isAssignableFrom(BasicAuthentication::class.java) ?: false
    }

}