package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import java.time.Duration

data class TokenCreationParams(
    val clientId: String,
    val ipAddress: String?,
    val ttl: Duration
) {
    init {
        validateFiled(clientId.isNotBlank()) { "clientId cannot be blank" }
    }
}