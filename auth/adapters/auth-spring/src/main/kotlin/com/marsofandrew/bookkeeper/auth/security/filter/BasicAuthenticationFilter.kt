package com.marsofandrew.bookkeeper.auth.security.filter

import com.marsofandrew.bookkeeper.auth.security.authentication.BasicAuthentication
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
internal class BasicAuthenticationFilter : AbstractAuthenticationFilter() {

    override fun getAuthentication(request: HttpServletRequest): Authentication? {

        return when {
            request.isBasic -> BasicAuthentication(request.basicAuth)
            else -> null
        }
    }

    private val HttpServletRequest.isBasic: Boolean
        get() = getHeader(HttpHeaders.AUTHORIZATION)?.startsWith("Basic") ?: false

    private val HttpServletRequest.basicAuth: String
        get() = getHeader(HttpHeaders.AUTHORIZATION).removePrefix("Basic").trim()
}
