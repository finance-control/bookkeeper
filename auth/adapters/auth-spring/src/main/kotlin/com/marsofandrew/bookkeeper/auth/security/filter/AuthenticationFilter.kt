package com.marsofandrew.bookkeeper.auth.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

internal abstract class AuthenticationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val auth = getAuthentication(request)

        auth?.let { SecurityContextHolder.getContext().authentication = it }
        filterChain.doFilter(request, response)
    }

    protected abstract fun getAuthentication(request: HttpServletRequest): Authentication?
}
