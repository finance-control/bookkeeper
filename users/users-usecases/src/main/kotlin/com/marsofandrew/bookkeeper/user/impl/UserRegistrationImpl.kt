package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.user.UnregisteredUser
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserRegistration
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserCredentialsSetter
import java.time.Clock
import org.apache.logging.log4j.LogManager

class UserRegistrationImpl(
    private val userStorage: UserStorage,
    private val userCredentialsSetter: UserCredentialsSetter,
    private val transactionExecutor: TransactionExecutor,
    private val clock: Clock
) : UserRegistration {

    private val logger = LogManager.getLogger()

    override fun register(unregisteredUser: UnregisteredUser): NumericId<User> {
        val forSave = unregisteredUser.toUser()
        return transactionExecutor.execute {
            val user = userStorage.create(forSave)
            userCredentialsSetter.set(
                userId = user.id,
                userRawCredentials = unregisteredUser.rawCredentials
            )
            user.id
        }.also {
            logger.info("User with id $it was created")
        }
    }

    private fun UnregisteredUser.toUser(): User {
        val now = clock.instant()
        return User(
            id = NumericId.unidentified(),
            version = Version(0),
            name = name,
            createdAt = now,
            updatedAt = now
        )
    }
}
