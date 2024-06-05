package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.credentials.UserEmailSelection
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserEmailSelectorImplTest {

    private val userEmailSelection = mockk<UserEmailSelection>()

    private lateinit var userEmailSelector: UserEmailSelectorImpl

    @BeforeEach
    fun setup() {
        userEmailSelector = UserEmailSelectorImpl(userEmailSelection)
    }

    @Test
    fun `select when user exists returns email`() {
        val userId = 33.asId<User>()
        val email = Email("test@marsofandrew.com")

        every { userEmailSelection.select(userId.value.asId()) } returns email

        val result = userEmailSelector.select(userId)

        result shouldBe email
    }
}