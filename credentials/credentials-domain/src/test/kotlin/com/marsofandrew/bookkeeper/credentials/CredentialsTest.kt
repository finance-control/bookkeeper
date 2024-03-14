package com.marsofandrew.bookkeeper.credentials

import com.marsofandrew.bookkeeper.base.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.email.Email
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CredentialsTest {

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "\t", "\n"])
    fun `constructor throws exception when password is blanc`(password: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            Credentials(
                email = Email("test@test.com"),
                password = password
            )
        }
    }

    @Test
    fun `constructor when correct values are set creates Credentials`() {
        val email = Email("test@test.com")
        val password = "pass"

        val result = Credentials(
            email = email,
            password = password
        )

        result.email shouldBe email
        result.password shouldBe password
    }
}