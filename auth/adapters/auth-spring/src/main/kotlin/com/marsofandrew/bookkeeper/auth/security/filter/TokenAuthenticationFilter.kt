package com.marsofandrew.bookkeeper.auth.security.filter

import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.auth.security.authentication.TokenAuthentication
import com.marsofandrew.bookkeeper.auth.security.util.X_CLIENT_ID_HEADER
import com.marsofandrew.bookkeeper.auth.security.util.X_FORWARDED_FOR
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.header.Header
import org.springframework.stereotype.Service

@Service
internal class TokenAuthenticationFilter: AbstractAuthenticationFilter() {

    override fun getAuthentication(request: HttpServletRequest): Authentication? {
        return when {
            request.isTokenAuth -> TokenAuthentication(request.token)
            else -> null
        }
    }
}

private val HttpServletRequest.isTokenAuth: Boolean
    get() = getHeader(HttpHeaders.AUTHORIZATION)?.startsWith("Bearer") ?: false

private val HttpServletRequest.token: String
    get() = getHeader(HttpHeaders.AUTHORIZATION).removePrefix("Bearer").trim()
