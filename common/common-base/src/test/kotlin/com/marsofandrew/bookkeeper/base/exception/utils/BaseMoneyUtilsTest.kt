package com.marsofandrew.bookkeeper.base.exception.utils

import com.marsofandrew.bookkeeper.base.utils.sumOfNullable
import com.marsofandrew.bookkeeper.base.utils.summarize
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BaseMoneyUtilsTest {

    @Test
    fun `sumOfNullable when 2 nulls are provided returns null`() {
        sumOfNullable<Long>(null, null) { l, r -> l + r } shouldBe null
    }

    @Test
    fun `sumOfNullable when left is not null and right is null returns left`() {
        sumOfNullable<Long>(5, null) { l, r -> l + r } shouldBe 5
    }

    @Test
    fun `sumOfNullable when right is not null and left is null returns right`() {
        sumOfNullable<Long>(null, 5) { l, r -> l + r } shouldBe 5
    }

    @Test
    fun `sumOfNullable when both parameters are not null returns sum`() {
        sumOfNullable<Long>(5, 3) { l, r -> l + r } shouldBe 8
    }

    @Test
    fun `summarize when currencies are different returns list that contains both currencies`() {
        val left = listOf(Money(Currency.EUR, 10, 1))
        val right = listOf(Money(Currency.USD, 108, 1))

        summarize(left, right) shouldContainExactlyInAnyOrder left + right
    }

    @Test
    fun `summarize when currencies are the same returns list that sum of amount`() {
        val left = listOf(Money(Currency.EUR, 10, 1))
        val right = listOf(Money(Currency.EUR, 108, 1))

        summarize(left, right) shouldContainExactlyInAnyOrder listOf(Money(Currency.EUR, 118, 1))
    }
}
