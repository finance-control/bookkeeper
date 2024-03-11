package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.UnregisteredUser
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserCredentialsSetter
import java.time.Clock

class UserRegistrationImpl(
    private val userStorage: UserStorage,
    private val userCredentialsSetter: UserCredentialsSetter,
    private val transactionalExecution: TransactionalExecution,
    private val clock: Clock
) : UserRegistration {

    override fun register(unregisteredUser: UnregisteredUser): NumericId<User> {
        val forSave = unregisteredUser.toUser()
        return transactionalExecution.execute {
            val user = userStorage.create(forSave)
            userCredentialsSetter.set(
                userId = user.id,
                userRawCredentials = unregisteredUser.rawCredentials
            )
            user.id
        }
    }

    private fun UnregisteredUser.toUser(): User {
        val now = clock.instant()
        return User(
            id = NumericId.unidentified(),
            version = Version(0),
            name = name,
            surname = surname,
            createdAt = now,
            updatedAt = now
        )
    }
}
