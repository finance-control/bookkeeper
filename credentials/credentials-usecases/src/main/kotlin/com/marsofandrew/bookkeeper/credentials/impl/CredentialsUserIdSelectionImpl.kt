package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.credentials.Credentials
import com.marsofandrew.bookkeeper.credentials.CredentialsUserIdSelection
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encryptor.CredentialsEncryptor
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

class CredentialsUserIdSelectionImpl(
    private val credentialsEncryptor: CredentialsEncryptor,
    private val credentialsStorage: CredentialsStorage,
) : CredentialsUserIdSelection {

    override fun select(credentials: Credentials): NumericId<User>? {
        return credentialsStorage.findByEmail(credentials.email)
            ?.takeIf { credentialsEncryptor.matches(credentials.password, it.password) }
            ?.userId
    }
}
