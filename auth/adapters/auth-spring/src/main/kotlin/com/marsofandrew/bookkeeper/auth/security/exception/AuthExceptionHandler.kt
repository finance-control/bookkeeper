package com.marsofandrew.bookkeeper.auth.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.marsofandrew.bookkeeper.auth.exception.AuthErrors
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

internal class AuthExceptionHandler : AuthenticationEntryPoint {

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        when (authException) {
            is InsufficientAuthenticationException -> handleUnauthorized(authException, response)
            is ForbiddenAuthException -> handleForbidden(authException, response)
            is InternalAuthError -> handleInternal(authException, response)
        }
    }

    private fun handleUnauthorized(authException: AuthenticationException, response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

    private fun handleForbidden(authException: AuthenticationException, response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_FORBIDDEN

        val error = authException.message ?: ""
        response.writer.write(objectMapper.writeValueAsString(AuthErrors(listOf(error))))
    }

    private fun handleInternal(authException: AuthenticationException, response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
    }
}