package com.marsofandrew.bookkeeper.properties

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class CurrencyTest {

    @Test
    fun `byCodeOrThrow throws exception when code does not exist`() {
        shouldThrowExactly<IllegalArgumentException> {
            Currency.byCodeOrThrow("yy")
        }
    }

    @ParameterizedTest
    @EnumSource(Currency::class)
    fun `byCodeOrThrow when correct code is passed returns Currency`(initialCurrency: Currency) {
        Currency.byCodeOrThrow(initialCurrency.code) shouldBe initialCurrency
    }
}