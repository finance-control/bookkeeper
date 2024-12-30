package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.credentials.CredentialsSetting
import com.marsofandrew.bookkeeper.credentials.RawUserCredentials
import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encryptor.CredentialsEncryptor
import com.marsofandrew.bookkeeper.credentials.exception.EmailAlreadyInUseException
import java.time.Clock
import java.time.Instant
import org.apache.logging.log4j.LogManager

class CredentialsSettingImpl(
    private val credentialsEncryptor: CredentialsEncryptor,
    private val credentialsStorage: CredentialsStorage,
    private val clock: Clock,
    private val transactionExecutor: TransactionExecutor
) : CredentialsSetting {

    private val logger = LogManager.getLogger()

    override fun set(credential: RawUserCredentials) {
        transactionExecutor.execute {
            val currentCredentials = credentialsStorage.findByUserId(credential.userId)
            if (credentialsStorage.existByEmailAndNotOwnsById(credential.email, currentCredentials?.userId)) {
                throw EmailAlreadyInUseException(email = credential.email)
            }
            val encrypted = credential.toCredentials(clock.instant(), currentCredentials?.version)
            credentialsStorage.createOrUpdate(encrypted)
        }.also {
            logger.info("Credentials for user ${credential.userId} were updated")
        }
    }

    private fun RawUserCredentials.toCredentials(now: Instant, version: Version?) = UserCredentials(
        userId = userId,
        email = email,
        password = credentialsEncryptor.encode(password),
        updatedAt = now,
        version = version ?: Version(0)
    )
}
