package com.marsofandrew.bookkeeper.tokens.access.entity

import com.marsofandrew.bookkeeper.base.model.asVersion
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.tokens.TokenCredentials
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Entity
@Table(name = "app_tokens")
internal data class TokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, allocationSize = ALLOCATION_SIZE)
    var id: Long?,
    val clientId: String,
    val userId: Long,
    val token: String,
    var ipAddress: String?,
    var createdAt: Instant,
    var expiredAt: Instant,
    @Version
    var version: Int,
) : BaseEntity<TokenCredentials> {

    override fun toModel() = TokenCredentials(
        id = requireNotNull(id).asId(),
        userId = userId.asId(),
        clientId = clientId,
        ipAddress = ipAddress,
        createdAt = createdAt,
        expiredAt = expiredAt,
        token = token,
        version = version.asVersion()
    )

    companion object {
        const val ALLOCATION_SIZE = 1000
        const val SEQUENCE_NAME = "app_tokens_id_seq"
    }
}

internal fun TokenCredentials.toTokenEntity(): TokenEntity = TokenEntity(
    id = id.rawValue,
    clientId = clientId,
    userId = userId.value,
    token = token,
    ipAddress = ipAddress,
    createdAt = createdAt,
    expiredAt = expiredAt,
    version = version.value
)
