package com.marsofandrew.bookkeeper.tokens

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.tokens.user.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class TokenCredentials(
    val userId: NumericId<User>,
    val clientId: String,
    val ipAddress: String?,
    val createdAt: Instant,
    val expiredAt: Instant,
    val token: String,
    override val version: Version
) : DomainModel {
    override val id: StringId<TokenCredentials> = token.asId()

    init {
        validateFiled(createdAt > LocalDateTime.of(2024, 9, 1, 0, 0).toInstant(ZoneOffset.UTC)) {
            "Invalid created_At for token"
        }
    }
}
