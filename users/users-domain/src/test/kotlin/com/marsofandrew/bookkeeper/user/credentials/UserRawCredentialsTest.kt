package com.marsofandrew.bookkeeper.user.credentials

import com.marsofandrew.bookkeeper.base.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.email.Email
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UserRawCredentialsTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "\t", "\n"])
    fun `constructor when password is blank throws exception`(password: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            UserRawCredentials(
                email = Email("test@test.com"),
                password = password
            )
        }
    }

    @Test
    fun `constructor when all values are correct creates UserRawCredentials`() {
        val email = Email("test@test.com")
        val password = "myPassword"

        val result = UserRawCredentials(
            email = email,
            password = password
        )

        result.email shouldBe email
        result.password shouldBe password
    }
}