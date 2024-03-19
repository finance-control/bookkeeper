package com.marsofandrew.bookkeeper.properties.email

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "test", "test.com", "ytr@yyy"])
    fun `constructor throws exception when value does not match regexp`(emailStr: String) {
        shouldThrowExactly<ObjectCreateValidationException> {
            Email(emailStr)
        }
    }

    @Test
    fun `constructor when correct values are set creates object`() {
        val inner = "test@test.com"

        val email = Email(inner)

        email.value shouldBe inner
    }
}