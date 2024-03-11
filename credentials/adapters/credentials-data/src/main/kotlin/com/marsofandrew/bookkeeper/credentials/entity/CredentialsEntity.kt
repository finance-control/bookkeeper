package com.marsofandrew.bookkeeper.credentials.entity

import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Entity
@Table(name = "credentials", schema = "bookkeeper")
internal data class CredentialsEntity(
    @Id
    var userId: Long,
    var email: String,
    var password: String,
    var updatedAt: Instant,
    @Version
    var version: Int
) : BaseEntity<UserCredentials, CredentialsEntity> {
    override fun toModel(): UserCredentials = UserCredentials(
        userId = userId.asId(),
        email = Email(email),
        password = password,
        updatedAt = updatedAt,
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )
}

internal fun UserCredentials.toCredentialsEntity() = CredentialsEntity(
    userId = userId.value,
    email = email.value,
    password = password,
    updatedAt = updatedAt,
    version = version.value
)
