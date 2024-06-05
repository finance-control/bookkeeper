package com.marsofandrew.bookkeeper.credentials.impl

import com.marsofandrew.bookkeeper.credentials.access.CredentialsStorage
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.credentials.userCredentials
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserEmailSelectionImplTest {

    private val credentialsStorage = mockk<CredentialsStorage>()

    private lateinit var userEmailSelectionImpl: UserEmailSelectionImpl

    @BeforeEach
    fun setup() {
        userEmailSelectionImpl = UserEmailSelectionImpl(credentialsStorage)
    }

    @Test
    fun `select when user exists returns email`() {
        val userId = 44.asId<User>()
        val expectedEmail = Email("test@email.com")

        every { credentialsStorage.findByUserIdOrThrow(userId) } returns userCredentials(userId){
            email = expectedEmail
        }

        val result = userEmailSelectionImpl.select(userId)

        result shouldBe expectedEmail
    }

}