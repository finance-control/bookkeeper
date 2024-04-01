package com.marsofandrew.bookkeeper.logging.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


@Service
internal class RequestIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            MDC.put(MDC_UUID_TOKEN_KEY, UUID.randomUUID().toString())
            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            logger.error("Exception occurred in filter while setting UUID for logs", ex)
        } finally {
            MDC.remove(MDC_UUID_TOKEN_KEY)
        }
    }

    override fun isAsyncDispatch(request: HttpServletRequest): Boolean {
        return false
    }

    override fun shouldNotFilterErrorDispatch(): Boolean {
        return false
    }

    private companion object {
        const val MDC_UUID_TOKEN_KEY = "RequestId"
    }
}
