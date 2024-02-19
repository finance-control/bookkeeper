package com.marsofandrew.bookkeeper.auth.security.provider

import com.marsofandrew.bookkeeper.auth.UserAuthentication
import com.marsofandrew.bookkeeper.auth.security.authentication.FakeAuthentication
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import org.springframework.security.authentication.AuthenticationProvider

import org.springframework.security.core.Authentication


internal class FakeAuthenticationProvider(
    private val userAuthentication: UserAuthentication
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val auth = authentication as FakeAuthentication
        val userId = userAuthentication.authenticate(auth.userId)

        return UserIdToken(userId)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.isAssignableFrom(FakeAuthentication::class.java) ?: false
    }

}