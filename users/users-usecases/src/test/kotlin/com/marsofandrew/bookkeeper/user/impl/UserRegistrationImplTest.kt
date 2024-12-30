package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.transaction.TestTransactionExecutor
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserCredentialsSetter
import com.marsofandrew.bookkeeper.user.fixture.unregisteredUser
import com.marsofandrew.bookkeeper.user.fixture.user
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserRegistrationImplTest {

    private val userStorage = mockk<UserStorage>()
    private val userCredentialsSetter = mockk<UserCredentialsSetter>(relaxUnitFun = true)
    private val clock = Clock.fixed(Instant.now(), ZoneId.of("Z"))

    private lateinit var userRegistrationImpl: UserRegistrationImpl

    @BeforeEach
    fun setup() {
        userRegistrationImpl =
            UserRegistrationImpl(userStorage, userCredentialsSetter, TestTransactionExecutor(), clock)
    }

    @Test
    fun `register when unregisters user is provided then registers user`() {
        val unregisteredUser = unregisteredUser()
        val unidentifiedUser = user(NumericId.unidentified()) {
            name = unregisteredUser.name
            createdAt = clock.instant()
            updatedAt = clock.instant()
        }

        val identifiedUser = unidentifiedUser.copy(id = 5.asId())

        every { userStorage.create(unidentifiedUser) } returns identifiedUser

        val resultUserId = userRegistrationImpl.register(unregisteredUser)

        resultUserId shouldBe identifiedUser.id
        verify(exactly = 1) { userStorage.create(unidentifiedUser) }
        verify(exactly = 1) { userCredentialsSetter.set(identifiedUser.id, unregisteredUser.rawCredentials) }
    }
}