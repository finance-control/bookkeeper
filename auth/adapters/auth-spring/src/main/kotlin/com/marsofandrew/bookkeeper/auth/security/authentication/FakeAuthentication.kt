package com.marsofandrew.bookkeeper.auth.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

internal class FakeAuthentication(val userId: String) :
    AbstractAuthenticationToken(listOf()) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = null
}
