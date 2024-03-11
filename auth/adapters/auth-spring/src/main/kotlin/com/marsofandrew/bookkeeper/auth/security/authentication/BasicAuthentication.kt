package com.marsofandrew.bookkeeper.auth.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

internal class BasicAuthentication(
    val credentials: String
) : AbstractAuthenticationToken(listOf()) {
    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any {
        TODO("Not yet implemented")
    }
}