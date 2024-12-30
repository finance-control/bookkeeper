package com.marsofandrew.bookkeeper.user.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import java.time.Instant

data class UserFixture(
    val id: NumericId<User>
) {
    var version: Version = Version(0)
    var name: String = "name"
    var createdAt: Instant = Instant.now()
    var updatedAt: Instant = createdAt

    fun build() = User(
        id = id,
        version = version,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}