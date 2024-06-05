package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserSelection
import com.marsofandrew.bookkeeper.user.UserWithEmail
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserEmailSelector

class UserSelectionImpl(
    private val userStorage: UserStorage,
    private val userEmailSelector: UserEmailSelector
) : UserSelection {

    override fun select(id: NumericId<User>): UserWithEmail {
        val user =  userStorage.findByIdOrThrow(id)
        val email = userEmailSelector.select(id)

        return UserWithEmail(user, email)
    }
}
