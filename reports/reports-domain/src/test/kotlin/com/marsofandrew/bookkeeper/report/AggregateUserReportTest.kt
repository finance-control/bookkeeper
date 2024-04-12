package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.category.Category
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class AggregateUserReportTest {

    @Test
    fun `plus throws exception when userIds are different`() {
        val report1 = AggregatedUserReport(
            userId = 1.asId(),
            periods = listOf(LocalDate.now()),
            expenses = Report.empty(),
            earnings = Report.empty(),
            transfers = Report.empty(),
            total = emptyList()
        )

        val report2 = AggregatedUserReport(
            userId = 2.asId(),
            periods = listOf(LocalDate.now()),
            expenses = Report.empty(),
            earnings = Report.empty(),
            transfers = Report.empty(),
            total = emptyList()
        )
        shouldThrowExactly<IllegalArgumentException> {
            report1 + report2
        }
    }

    @Test
    fun `plus summarizes different reports`() {
        val now = LocalDate.now()
        val report1 = AggregatedUserReport(
            userId = 1.asId(),
            periods = listOf(now),
            expenses = Report(
                byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 10, 0))),
                total = listOf(PositiveMoney(Currency.EUR, 10, 0))
            ),
            earnings = Report(
                byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 8, 0))),
                total = listOf(PositiveMoney(Currency.EUR, 10, 0))
            ),
            transfers = Report.empty(),
            total = listOf(Money(Currency.EUR, -2, 0))
        )

        val report2 = AggregatedUserReport(
            userId = 1.asId(),
            periods = listOf(now.minusDays(1)),
            expenses = Report(
                byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.USD, 10, 0))),
                total = listOf(PositiveMoney(Currency.USD, 10, 0))
            ),
            earnings = Report.empty(),
            transfers = Report(
                byCategory = mapOf(
                    1.asId<Category>() to listOf(
                        Money(Currency.EUR, -10, 0),
                        Money(Currency.USD, 11, 0)
                    )
                ),
                total = listOf(
                    Money(Currency.EUR, -10, 0),
                    Money(Currency.USD, 11, 0)
                )
            ),
            total = listOf(
                Money(Currency.USD, 1, 0),
                Money(Currency.EUR, -10, 0)
            )
        )

        val expected = AggregatedUserReport(
            userId = 1.asId(),
            periods = listOf(now, now.minusDays(1)),
            expenses = report1.expenses + report2.expenses,
            earnings = report1.earnings + report2.earnings,
            transfers = report1.transfers + report2.transfers,
            total = listOf(
                Money(Currency.EUR, -12, 0),
                Money(Currency.USD, 1, 0),
            )
        )
        val result = report1 + report2

        result shouldBe expected
    }
}