package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.credentials.user.User
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.properties.id.asId
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class RawUserCredentialsTest {

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t", "\n"])
    fun `constructor throws exception when password is blanc`(password: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            RawUserCredentials(
                userId = 5.asId(),
                email = Email("test@test.com"),
                password = password
            )
        }
    }

    @Test
    fun `constructor when correct values are set creates RawUserCredentials`() {
        val userId = 5.asId<User>()
        val email = Email("test@test.com")
        val password = "pass"

        val result = RawUserCredentials(
            userId = userId,
            email = email,
            password = password
        )

        result.userId shouldBe userId
        result.email shouldBe email
        result.password shouldBe password
    }
}