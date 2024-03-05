package com.marsofandrew.bookkeeper.report.user

import com.marsofandrew.bookkeeper.base.exception.orElseThrow
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface UserProvider {

    fun findUserByIdOrThrow(userId: NumericId<User>): User = findByUserId(userId).orElseThrow(userId)
    fun findByUserId(userId: NumericId<User>): User?
}