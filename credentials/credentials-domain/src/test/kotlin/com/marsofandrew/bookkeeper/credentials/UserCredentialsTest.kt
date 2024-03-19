package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UserCredentialsTest {

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t", "\n"])
    fun `constructor throws exception when password is blanc`(password: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            UserCredentials(
                userId = 5.asId(),
                email = Email("test@test.com"),
                password = password,
                updatedAt = Instant.now(),
                version = Version(0)
            )
        }
    }

    @Test
    fun `constructor when correct values are set creates UserCredentials`() {
        val userId = 5.asId<User>()
        val email = Email("test@test.com")
        val password = "pass"
        val updatedAt = Instant.now()
        val version = Version(0)

        val result = UserCredentials(
            userId = userId,
            email = email,
            password = password,
            updatedAt = updatedAt,
            version = version
        )

        result.userId shouldBe userId
        result.email shouldBe email
        result.password shouldBe password
        result.updatedAt shouldBe updatedAt
        result.version shouldBe version
    }
}