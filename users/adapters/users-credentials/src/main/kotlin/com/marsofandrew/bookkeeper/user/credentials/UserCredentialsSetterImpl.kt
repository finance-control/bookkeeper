package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.credentials.CredentialsSetting
import com.marsofandrew.bookkeeper.credentials.RawUserCredentials
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import org.springframework.stereotype.Service

@Service
internal class UserCredentialsSetterImpl(
    private val credentialsSetting: CredentialsSetting
) : UserCredentialsSetter {

    override fun set(userId: NumericId<User>, userRawCredentials: UserRawCredentials) {
        credentialsSetting.set(userRawCredentials.toRawUserCredentials(userId))
    }

    private fun UserRawCredentials.toRawUserCredentials(userId: NumericId<User>) = RawUserCredentials(
        userId = userId.value.asId(),
        email = email,
        password = password
    )
}