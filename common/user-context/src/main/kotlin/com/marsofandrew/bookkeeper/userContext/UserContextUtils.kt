package com.marsofandrew.bookkeeper.userContext

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

private const val DEFAULT_IP_ADDRESS = "0.0.0.0"
private const val CLIENT_ID_HEADER = "X-Client-Id"

fun getRequestIpAddress(): String = getRequestAttributes()?.request?.remoteAddr ?: DEFAULT_IP_ADDRESS

fun getRequestClientId(): String? = getRequestAttributes()?.request?.getHeader(CLIENT_ID_HEADER)

private fun getRequestAttributes(): ServletRequestAttributes? =
    (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes?)