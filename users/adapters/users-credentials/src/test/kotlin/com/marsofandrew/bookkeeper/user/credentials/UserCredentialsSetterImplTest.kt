package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.credentials.CredentialsSetting
import com.marsofandrew.bookkeeper.credentials.RawUserCredentials
import com.marsofandrew.bookkeeper.credentials.exception.EmailAlreadyInUseException
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.exception.UserEmailIsAlreadyInUseException
import com.marsofandrew.bookkeeper.user.fixture.userRawCredentials
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserCredentialsSetterImplTest {

    private val credentialsSetting = mockk<CredentialsSetting>(relaxUnitFun = true)

    private lateinit var userCredentialsSetterImpl: UserCredentialsSetterImpl

    @BeforeEach
    fun setup() {
        userCredentialsSetterImpl = UserCredentialsSetterImpl(credentialsSetting)
    }

    @Test
    fun `set when credentialsSetting throws EmailAlreadyInUseException then rethrows UserEmailIsAlreadyInUseException`() {
        every { credentialsSetting.set(any()) } throws EmailAlreadyInUseException(Email("testy@hh.com"))

        shouldThrowExactly<UserEmailIsAlreadyInUseException> {
            userCredentialsSetterImpl.set(2.asId(), userRawCredentials())
        }
    }

    @Test
    fun `set when values are correct then credentials are updated`() {
        val userId = 5.asId<User>()
        val userRawCredentials = userRawCredentials()

        userCredentialsSetterImpl.set(userId, userRawCredentials)

        verify(exactly = 1) {
            credentialsSetting.set(
                RawUserCredentials(
                    userId = userId.value.asId(),
                    email = userRawCredentials.email,
                    password = userRawCredentials.password
                )
            )
        }
    }
}