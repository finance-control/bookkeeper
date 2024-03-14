package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CredentialsUserIdSelection {

    fun select(credentials: Credentials): NumericId<User>?
}
