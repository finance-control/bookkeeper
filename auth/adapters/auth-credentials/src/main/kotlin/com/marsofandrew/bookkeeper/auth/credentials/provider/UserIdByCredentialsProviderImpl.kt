package com.marsofandrew.bookkeeper.auth.credentials.provider

import com.marsofandrew.bookkeeper.auth.provider.UserIdByCredentialsProvider
import com.marsofandrew.bookkeeper.credentials.Credentials
import com.marsofandrew.bookkeeper.credentials.CredentialsUserIdSelection
import com.marsofandrew.bookkeeper.properties.email.Email
import org.springframework.stereotype.Service

@Service
internal class UserIdByCredentialsProviderImpl(
    private val credentialsUserIdSelection: CredentialsUserIdSelection
) : UserIdByCredentialsProvider {

    override fun getIdByKey(email: Email, password: String): Long? {
        return credentialsUserIdSelection.select(Credentials(email, password))?.value
    }
}