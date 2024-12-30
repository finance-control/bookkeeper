package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.Instant

data class User(
    override val id: NumericId<User>,
    override val version: Version,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant
) : DomainModel {

    init {
        validateFiled(name.isNotBlank()) { "name is blank" }
        validateFiled(createdAt.epochSecond > APP_EPOCH_SECONDS) { "User created before 2024-03-01" }
        validateFiled(updatedAt >= createdAt) { "User updated before creation" }
    }

    companion object {
        const val APP_EPOCH_SECONDS: Long = 1710028800 // 2024-03-10T00:00:00Z
    }
}

