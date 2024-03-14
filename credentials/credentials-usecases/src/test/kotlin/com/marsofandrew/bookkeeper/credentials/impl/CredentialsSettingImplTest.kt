package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.credentials.UserCredentials
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encoder.CredentialsEncoder
import com.marsofandrew.bookkeeper.credentials.exception.EmailAlreadyInUseException
import com.marsofandrew.bookkeeper.credentials.rawUserCredentials
import com.marsofandrew.bookkeeper.credentials.userCredentials
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CredentialsSettingImplTest {

    private val credentialsEncoder = mockk<CredentialsEncoder>()
    private val credentialsStorage = mockk<CredentialsStorage>(relaxUnitFun = true)
    private val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    private val transactionExecutor = TestTransactionExecutor()

    private lateinit var credentialsSettingImpl: CredentialsSettingImpl

    @BeforeEach
    fun setup() {
        credentialsSettingImpl =
            CredentialsSettingImpl(credentialsEncoder, credentialsStorage, clock, transactionExecutor)
    }

    @Test
    fun `set throws exception when email already in use`() {
        val userEmail = Email("test@test.com")
        val credentials = rawUserCredentials(5.asId()) {
            email = userEmail
        }

        every { credentialsStorage.findByUserId(credentials.userId) } returns null
        every { credentialsStorage.existByEmailAndNotOwnsById(userEmail, any()) } returns true

        shouldThrowExactly<EmailAlreadyInUseException> {
            credentialsSettingImpl.set(credentials)
        }
    }

    @Test
    fun `set when user exists and credentials are different then updates credentials`() {
        val userEmail = Email("test@test.com")
        val credentials = rawUserCredentials(5.asId()) {
            email = userEmail
        }

        val userCredentials = userCredentials(credentials.userId)

        every { credentialsStorage.findByUserId(credentials.userId) } returns userCredentials
        every { credentialsStorage.existByEmailAndNotOwnsById(userEmail, any()) } returns false
        every { credentialsEncoder.encode(credentials.password) } returns credentials.password
        credentialsSettingImpl.set(credentials)

        verify(exactly = 1) {
            credentialsStorage.createOrUpdate(
                UserCredentials(
                    userId = credentials.userId,
                    email = credentials.email,
                    password = credentials.password,
                    updatedAt = clock.instant(),
                    version = userCredentials.version
                )
            )
        }
    }

    @Test
    fun `set when provided user does not exists in credentials storage when creates credentials`() {
        val userEmail = Email("test@test.com")
        val credentials = rawUserCredentials(5.asId()) {
            email = userEmail
        }

        every { credentialsStorage.findByUserId(credentials.userId) } returns null
        every { credentialsStorage.existByEmailAndNotOwnsById(userEmail, any()) } returns false
        every { credentialsEncoder.encode(credentials.password) } returns credentials.password
        credentialsSettingImpl.set(credentials)

        verify(exactly = 1) {
            credentialsStorage.createOrUpdate(
                UserCredentials(
                    userId = credentials.userId,
                    email = credentials.email,
                    password = credentials.password,
                    updatedAt = clock.instant(),
                    version = Version(0)
                )
            )
        }
    }
}
