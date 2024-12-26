package com.marsofandrew.bookkeeper.user.impl

import com.marsofandrew.bookkeeper.base.exception.DomainModelNotFoundException
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.user.User
import com.marsofandrew.bookkeeper.user.access.UserStorage
import com.marsofandrew.bookkeeper.user.token.UserTokenCreator
import com.marsofandrew.bookkeeper.user.fixture.user
import com.marsofandrew.bookkeeper.user.token.UserToken
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.Duration
import java.time.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserLoginImplTest {

    private val userStorage = mockk<UserStorage>(relaxUnitFun = true)
    private val userTokenCreator: UserTokenCreator = mockk(relaxUnitFun = true)


    private lateinit var userLoginImpl: UserLoginImpl

    @BeforeEach
    fun setup() {
        userLoginImpl = UserLoginImpl(userStorage, userTokenCreator)
        every { userTokenCreator.getOrCreate(any(), any(), any(), any()) } returns
                UserToken(token = "token", Instant.now().plusSeconds(1000))
    }

    @Test
    fun `login rethrows exception when user is absent`() {
        val userId = 55.asId<User>()

        every { userStorage.findByIdOrThrow(userId) } throws DomainModelNotFoundException(userId)

        shouldThrowExactly<DomainModelNotFoundException> {
            userLoginImpl.login(userId, "", "", Duration.ofHours(1))
        }
    }

    @Test
    fun `login when user is present in DB returns User`() {
        val user = user(54.asId())

        every { userStorage.findByIdOrThrow(user.id) } returns user

        val result = userLoginImpl.login(user.id, "", "", Duration.ofHours(1))

        result.user shouldBe user
    }
}
