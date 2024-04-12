package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.category.Category
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ReportTest {

    @Test
    fun `plus when 2 reports contains different categories then both categories are in the result report`() {
        val report1 = Report(
            byCategory = mapOf(4.asId<Category>() to listOf(Money(Currency.EUR, 5, 0))),
            total = listOf(Money(Currency.EUR, 5, 0))
        )

        val report2 = Report(
            byCategory = mapOf(5.asId<Category>() to listOf(Money(Currency.EUR, 7, 0))),
            total = listOf(Money(Currency.EUR, 7, 0))
        )

        val expected = Report(
            byCategory = mapOf(
                5.asId<Category>() to listOf(Money(Currency.EUR, 7, 0)),
                4.asId<Category>() to listOf(Money(Currency.EUR, 5, 0))
            ),
            total = listOf(Money(Currency.EUR, 12, 0))
        )

        val result = report1 + report2

        result shouldBe expected
    }

    @Test
    fun `plus when 2 reports contains different categories then summarize that moneys`() {
        val report1 = Report(
            byCategory = mapOf(5.asId<Category>() to listOf(Money(Currency.USD, 5, 0))),
            total = listOf(Money(Currency.USD, 5, 0))
        )

        val report2 = Report(
            byCategory = mapOf(5.asId<Category>() to listOf(Money(Currency.EUR, 7, 0))),
            total = listOf(Money(Currency.EUR, 7, 0))
        )

        val expected = Report(
            byCategory = mapOf(
                5.asId<Category>() to listOf(Money(Currency.USD, 5, 0), Money(Currency.EUR, 7, 0)),
            ),
            total = listOf(Money(Currency.USD, 5, 0), Money(Currency.EUR, 7, 0))
        )

        val result = report1 + report2

        result shouldBe expected
    }
}