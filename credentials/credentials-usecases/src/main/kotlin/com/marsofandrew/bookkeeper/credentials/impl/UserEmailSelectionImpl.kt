package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.credentials.UserEmailSelection
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId

class UserEmailSelectionImpl(
    private val credentialsStorage: CredentialsStorage
): UserEmailSelection {

    override fun select(userId: NumericId<User>): Email =
        credentialsStorage.findByUserIdOrThrow(userId).email
}