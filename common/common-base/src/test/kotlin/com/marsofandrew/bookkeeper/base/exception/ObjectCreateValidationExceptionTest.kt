package com.marsofandrew.bookkeeper.base.exception

import com.marsofandrew.bookkeeper.properties.exception.ObjectCreateValidationException
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import org.junit.jupiter.api.Test

internal class ObjectCreateValidationExceptionTest {

    @Test
    fun `validateField throws exception when predicate is false`() {
        shouldThrowExactly<ObjectCreateValidationException> {
            validateFiled(false) { "message" }
        }
    }

    @Test
    fun `validateField when predicate is true does nothing`() {
        shouldNotThrowAny {
            validateFiled(true) { "" }
        }
    }
}