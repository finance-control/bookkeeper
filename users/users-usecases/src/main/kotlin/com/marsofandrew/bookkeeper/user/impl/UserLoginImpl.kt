package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserLogin
import com.marsofandrew.bookkeeper.user.access.UserStorage

class UserLoginImpl(
    private val userStorage: UserStorage
): UserLogin {

    override fun login(id: NumericId<User>): User {
        return userStorage.findByIdOrThrow(id)
    }
}
