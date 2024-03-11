package com.marsofandrew.bookkeeper.user.access

import com.marsofandrew.bookkeeper.user.User

interface UserStorage {

    fun create(user: User): User
}