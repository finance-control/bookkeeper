package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.Instant

data class UserCredentials(
    val userId: NumericId<User>,
    val email: Email,
    val password: String,
    val updatedAt: Instant,
    override val version: Version,
) : DomainModel {

    override val id: NumericId<User> = userId

    init {
        check(password.isNotBlank()) { "password is blank" }
    }
}
