package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

interface UserSelection {

    fun select(id: NumericId<User>): UserWithEmail
}
