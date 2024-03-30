package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserSelection
import com.marsofandrew.bookkeeper.user.access.UserStorage

class UserSelectionImpl(
    private val userStorage: UserStorage
) : UserSelection {

    override fun select(id: NumericId<User>): User {
        return userStorage.findByIdOrThrow(id)
    }
}
