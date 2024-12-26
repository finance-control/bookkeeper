package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.Duration

interface UserLogin {

    fun login(id: NumericId<User>, clientId: String, ipAddress: String, ttl: Duration): UserWithToken
}
