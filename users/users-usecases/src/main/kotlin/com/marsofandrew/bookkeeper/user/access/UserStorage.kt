package com.marsofandrew.bookkeeper.user.access

import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.User

interface UserStorage {

    fun findByIdOrThrow(id: NumericId<User>): User = findById(id).orElseThrow(id)

    fun findById(id: NumericId<User>): User?

    fun create(user: User): User
}
