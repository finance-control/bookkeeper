package com.marsofandrew.bookkeeper.credentials.access

import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.NumericId

interface CredentialsStorage {

    fun findByUserId(userId: NumericId<User>): UserCredentials?
    fun findByEmail(email: Email): UserCredentials?

    fun existByEmailAndNotOwnsById(email: Email, userId: NumericId<User>?): Boolean

    fun createOrUpdate(userCredentials: UserCredentials)
}
