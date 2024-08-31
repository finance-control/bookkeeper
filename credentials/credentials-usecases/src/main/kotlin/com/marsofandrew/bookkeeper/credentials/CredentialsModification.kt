package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CredentialsModification {

    fun modify(userId: NumericId<User>, rawPassword: String)
}