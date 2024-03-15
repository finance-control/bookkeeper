package com.marsofandrew.bookkeeper.user.access.entity

import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Entity
@Table(name = "app_users", schema = "bookkeeper")
internal data class UserEntity(
    @Id
    var id: Long,
    @Version
    var version: Int,
    var name: String,
    var surname: String,
    var createdAt: Instant,
    var updatedAt: Instant
) : BaseEntity<User> {

    override fun toModel() = User(
        id = requireNotNull(id).asId(),
        version = com.marsofandrew.bookkeeper.base.model.Version(version),
        name = name,
        surname = surname,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

internal fun User.toUserEntity() = UserEntity(
    id = id.value,
    version = version.value,
    name = name,
    surname = surname,
    createdAt = createdAt,
    updatedAt = updatedAt,
)