package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.fixture.user
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserSelectionImplTest {

    private val userStorage = mockk<UserStorage>()

    private lateinit var userSelectionImpl: UserSelectionImpl

    @BeforeEach
    fun setup() {
        userSelectionImpl = UserSelectionImpl(userStorage)
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

        every { userStorage.findByIdOrThrow(user.id) } returns user

        val result = userSelectionImpl.select(user.id)

        result shouldBe user
    }
}
