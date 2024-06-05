package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

interface UserEmailSelector {

    fun select(userId: NumericId<User>): Email
}