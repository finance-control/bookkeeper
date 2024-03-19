package com.marsofandrew.bookkeeper.properties

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MoneyTest {

    @Test
    fun `plus throws exception when currencies are different`() {
        shouldThrowExactly<IllegalArgumentException> {
            Money(Currency.EUR, 10, 1) + Money(Currency.USD, 10, 1)
        }
    }

    @Test
    fun `plus when correct parameters are used summarize amounts amount`() {
        val left = Money(Currency.EUR, 54, 1)
        val right = Money(Currency.EUR, 24, 0)
        val expected = Money(Currency.EUR, 294, 1)

        left + right shouldBe expected
    }

    @Test
    fun `minus throws exception when currencies are different`() {
        shouldThrowExactly<IllegalArgumentException> {
            Money(Currency.EUR, 10, 1) - Money(Currency.USD, 10, 1)
        }
    }

    @Test
    fun `minus when correct parameters are used subtract amounts amount`() {
        val left = Money(Currency.EUR, 54, 1)
        val right = Money(Currency.EUR, 24, 0)
        val expected = Money(Currency.EUR, -186, 1)

        left - right shouldBe expected
    }

    @Test
    fun `equals when currencies are the same and the normalized values are the same returns 0`() {
        val left = Money(Currency.EUR, 1, 0)
        val right = Money(Currency.EUR, 1, 0)

        (left == right) shouldBe true
    }

    @Test
    fun `equals when currencies are different returns non zero`() {
        val left = Money(Currency.EUR, 1, 0)
        val right = Money(Currency.USD, 1, 0)

        (left == right) shouldBe false
    }

    @Test
    fun `equals when amounts are different returns non zero`() {
        val left = Money(Currency.EUR, 1, 0)
        val right = Money(Currency.EUR, 11, 1)

        (left == right) shouldBe false
    }

    @Test
    fun `hash code when digits are the same returns same hash code`() {
        val left = Money(Currency.EUR, 1, 0)
        val right = Money(Currency.EUR, 10, 1)

        (left.hashCode() == right.hashCode()) shouldBe true
    }
}

