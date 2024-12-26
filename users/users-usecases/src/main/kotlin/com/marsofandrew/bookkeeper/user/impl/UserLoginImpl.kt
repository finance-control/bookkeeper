package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.UserWithToken
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.token.UserTokenCreator
import java.time.Duration

class UserLoginImpl(
    private val userStorage: UserStorage,
    private val userTokenCreator: UserTokenCreator,
): UserLogin {

    override fun login(id: NumericId<User>, clientId: String, ipAddress: String, ttl: Duration): UserWithToken {
        val user = userStorage.findByIdOrThrow(id)
        // temporary avoid to pass Ip address for tokens
        val token = userTokenCreator.getOrCreate(id, clientId, null, ttl)
        return UserWithToken(user, token.token, token.expiredAt)
    }
}
