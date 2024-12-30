package com.marsofandrew.bookkeeper.user

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.email.Email
import com.marsofandrew.bookkeeper.user.credentials.UserRawCredentials
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UnregisteredUserTest {

    @Test
    fun `constructor when all values are correct creates UnregisteredUser`() {
        val name = "test"
        val rawCredentials = UserRawCredentials(
            email = Email("test@test.com"),
            password = "password"
        )

        val result = UnregisteredUser(
            name = name,
            rawCredentials = rawCredentials
        )

        result.name shouldBe name
        result.rawCredentials shouldBe rawCredentials
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "\t", "\n"])
    fun `constructor when name is blank throws exception`(name: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            UnregisteredUser(
                name = name,
                rawCredentials = UserRawCredentials(
                    email = Email("test@test.com"),
                    password = "password"
                )
            )
        }
    }
}
