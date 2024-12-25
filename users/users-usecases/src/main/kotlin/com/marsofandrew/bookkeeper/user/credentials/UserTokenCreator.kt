package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

interface UserTokenCreator {

    fun create(userId: NumericId<User>, clientId: String, ipAddress: String?): String
}