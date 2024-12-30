package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.credentials.CredentialsModification
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encryptor.CredentialsEncryptor
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import org.apache.logging.log4j.LogManager
import java.time.Clock

class CredentialsModificationImpl(
    private val credentialsEncryptor: CredentialsEncryptor,
    private val credentialsStorage: CredentialsStorage,
    private val clock: Clock,
    private val transactionExecutor: TransactionExecutor
) : CredentialsModification {

    private val logger = LogManager.getLogger()

    override fun modify(userId: NumericId<User>, rawPassword: String) = transactionExecutor.execute {
        val currentCredentials = credentialsStorage.findByUserIdOrThrow(userId)

        val updated = currentCredentials.copy(
            password = credentialsEncryptor.encode(rawPassword),
            updatedAt = clock.instant()
        )
        credentialsStorage.createOrUpdate(updated)
    }.also {
        logger.info("Credentials for user $userId were updated")
    }

}