package com.marsofandrew.bookkeeper.userContext

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserIdToken(
    val userId: Long
) : AbstractAuthenticationToken(listOf()) {

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = null
}