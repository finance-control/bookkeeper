package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserLogin {

    fun login(id: NumericId<User>): User
}