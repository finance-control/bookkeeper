package com.marsofandrew.bookkeeper.user.token

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import java.time.Duration

interface UserTokenCreator {

    fun getOrCreate(userId: NumericId<User>, clientId: String, ipAddress: String?, ttl: Duration): UserToken
}
