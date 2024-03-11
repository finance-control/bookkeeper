package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

interface UserCredentialsSetter {

    fun set(userId: NumericId<User>, userRawCredentials: UserRawCredentials)
}