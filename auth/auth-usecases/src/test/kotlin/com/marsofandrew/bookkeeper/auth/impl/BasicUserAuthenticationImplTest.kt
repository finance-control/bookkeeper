package com.marsofandrew.bookkeeper.auth.impl

import com.marsofandrew.bookkeeper.auth.exception.ForbiddenException
import com.marsofandrew.bookkeeper.auth.exception.IncorrectCredentialsException
import com.marsofandrew.bookkeeper.auth.provider.UserIdByCredentialsProvider
import com.marsofandrew.bookkeeper.properties.email.Email
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Base64
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BasicUserAuthenticationImplTest {

    private val userIdByCredentialsProvider = mockk<UserIdByCredentialsProvider>()


    private lateinit var basicUserAuthenticationImpl: BasicUserAuthenticationImpl

    @BeforeEach
    fun setup() {
        basicUserAuthenticationImpl = BasicUserAuthenticationImpl(userIdByCredentialsProvider)
    }

    @Test
    fun `getUserIdByAuth throws exception when password is absent`() {
        val auth = "xhshx"

        shouldThrowExactly<ForbiddenException> {
            basicUserAuthenticationImpl.getUserIdByAuth(Base64.getEncoder().encodeToString(auth.encodeToByteArray()))
        }
    }

    @Test
    fun `getUserIdByAuth throws exception when user is absent or credentials are incorrect`() {
        val email = Email("test@hh.com")
        val password = "pass"

        every { userIdByCredentialsProvider.getIdByCredentials(email, password) } returns null

        shouldThrowExactly<IncorrectCredentialsException> {
            basicUserAuthenticationImpl.getUserIdByAuth(
                Base64.getEncoder().encodeToString("${email.value}:$password".encodeToByteArray())
            )
        }
    }

    @Test
    fun `getUserIdByAuth when credentials are correct then returns userId`() {
        val email = Email("test@hh.com")
        val password = "pass"
        val userId = 158L

        every { userIdByCredentialsProvider.getIdByCredentials(email, password) } returns userId

        val result = basicUserAuthenticationImpl.getUserIdByAuth(
            Base64.getEncoder().encodeToString("${email.value}:$password".encodeToByteArray())
        )

        result shouldBe userId
    }
}