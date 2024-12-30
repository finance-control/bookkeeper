package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.encryptor.CredentialsEncryptor
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.credentials.userCredentials
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class CredentialsModificationImplTest {

    private val USER_ID = 5643.asId<User>()

    private val credentialsEncryptor = mockk<CredentialsEncryptor>()
    private val credentialsStorage = mockk<CredentialsStorage>(relaxUnitFun = true)
    private val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))
    private val transactionExecutor = TestTransactionExecutor()

    private lateinit var credentialsModification: CredentialsModificationImpl

    @BeforeEach
    fun setup() {
        credentialsModification = CredentialsModificationImpl(
            credentialsEncryptor = credentialsEncryptor,
            credentialsStorage = credentialsStorage,
            clock = clock,
            transactionExecutor = transactionExecutor
        )
    }

    @Test
    fun `modify throws exception when user is absent in DB`() {
        every { credentialsStorage.findByUserIdOrThrow(USER_ID) } throws RuntimeException()

        shouldThrow<RuntimeException> {credentialsModification.modify(USER_ID, "pass")}

        verify(exactly = 0) { credentialsStorage.createOrUpdate(any()) }
    }

    @Test
    fun `modify modifies password when user exists in DB`() {
        val credentials = userCredentials(USER_ID)
        val updatedCredentials = credentials.copy(
            updatedAt = clock.instant(),
            password = "new!pass",
        )

        every { credentialsStorage.findByUserIdOrThrow(USER_ID) } returns credentials
        every { credentialsEncryptor.encode(updatedCredentials.password) } returns updatedCredentials.password
        every { credentialsStorage.createOrUpdate(updatedCredentials) } returns Unit

        credentialsModification.modify(USER_ID, updatedCredentials.password)

        verify(exactly = 1) { credentialsStorage.createOrUpdate(updatedCredentials) }
    }
}