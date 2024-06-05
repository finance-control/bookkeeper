package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.UserWithEmail
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.credentials.UserEmailSelector
import com.marsofandrew.bookkeeper.user.fixture.user
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserSelectionImplTest {

    private val userStorage = mockk<UserStorage>()
    private val userEmailSelector = mockk<UserEmailSelector>()

    private lateinit var userSelectionImpl: UserSelectionImpl

    @BeforeEach
    fun setup() {
        userSelectionImpl = UserSelectionImpl(userStorage, userEmailSelector)
    }

    @Test
    fun `select throws exception when user is absent`() {
        val userId = 33.asId<User>()

        every { userStorage.findByIdOrThrow(userId) } throws DomainModelNotFoundException(userId)

        shouldThrowExactly<DomainModelNotFoundException> {
            userSelectionImpl.select(userId)
        }
    }

    @Test
    fun `select when user is present returns user`() {
        val user = user(543.asId())
        val email = Email("email@example.com")
        every { userStorage.findByIdOrThrow(user.id) } returns user
        every {  userEmailSelector.select(user.id) } returns email

        val result = userSelectionImpl.select(user.id)

        result shouldBe UserWithEmail(user, email)
    }
}
