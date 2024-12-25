package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.UserWithToken
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserTokenCreator

class UserLoginImpl(
    private val userStorage: UserStorage,
    private val userTokenCreator: UserTokenCreator,
): UserLogin {

    override fun login(id: NumericId<User>, clientId: String, ipAddress: String): UserWithToken {
        val user = userStorage.findByIdOrThrow(id)
        // temporary avoid to pass Ip address for tokens
        val token = userTokenCreator.create(id, clientId, null)
        return UserWithToken(user, token)
    }
}
