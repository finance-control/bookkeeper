package com.marsofandrew.bookkeeper.auth.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

internal class TokenAuthentication(
    val token: String,
): AbstractAuthenticationToken(listOf()) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = null
}
