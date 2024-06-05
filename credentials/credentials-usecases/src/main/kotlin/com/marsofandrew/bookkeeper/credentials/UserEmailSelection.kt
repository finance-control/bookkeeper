package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserEmailSelection {

    fun select(userId: NumericId<User>): Email
}