package com.marsofandrew.bookkeeper.tokens.access.entity

import com.marsofandrew.bookkeeper.base.model.asVersion
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import java.time.Instant

@Entity
@Table(name = "app_tokens")
internal data class TokenEntity(
    @EmbeddedId
    var tokenId: TokenId,
    var ipAddress: String?,
    var createdAt: Instant,
    var expiredAt: Instant,
    @Version
    var version: Int,
) : BaseEntity<TokenCredentials> {

    @Embeddable
    internal data class TokenId(
        val clientId: String,
        val userId: Long,
        val token: String
    )

    override fun toModel() = TokenCredentials(
        userId = tokenId.userId.asId(),
        clientId = tokenId.clientId,
        ipAddress = ipAddress,
        createdAt = createdAt,
        expiredAt = expiredAt,
        token = tokenId.token,
        version = version.asVersion()
    )
}

internal fun TokenCredentials.toTokenEntity(): TokenEntity = TokenEntity(
    tokenId = TokenEntity.TokenId(
        clientId = clientId,
        userId = userId.value,
        token = token
    ),
    ipAddress = ipAddress,
    createdAt = createdAt,
    expiredAt = expiredAt,
    version = version.value
)