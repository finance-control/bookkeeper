package com.marsofandrew.bookkeeper.properties.exception

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import org.junit.jupiter.api.Test

internal class ObjectCreateValidationExceptionTest {

    @Test
    fun `validateField throws exception when predicate is false`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            validateFiled(false) { "" }
        }
    }

    @Test
    fun `validateField do nothing when predicate is true`() {
        shouldNotThrowAny {
            validateFiled(true) { "" }
        }
    }
}
