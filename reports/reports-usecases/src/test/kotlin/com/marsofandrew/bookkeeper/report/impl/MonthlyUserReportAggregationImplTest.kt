package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MonthlyUserReportAggregationImplTest {

    private val monthlyUserReportStorage = mockk<MonthlyUserReportStorage>()

    private lateinit var monthlyReportAggregationImpl: MonthlyUserReportAggregationImpl

    @BeforeEach
    fun setup() {
        monthlyReportAggregationImpl = MonthlyUserReportAggregationImpl(monthlyUserReportStorage)
    }

    @Test
    fun `aggregate when reports is absent returns empty aggregated report`() {
        val endDate = YearMonth.from(LocalDate.now())
        val startDate = endDate.minusMonths(1)
        val userId = 5.asId<User>()

        every { monthlyUserReportStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns emptyList()

        val result = monthlyReportAggregationImpl.makeReport(userId, startDate, endDate)

        result.userId shouldBe userId
        result.periods shouldHaveSize 0
        result.earnings shouldBe Report.empty()
        result.expenses shouldBe Report.empty()
        result.transfers shouldBe Report.empty()
        result.total shouldBe emptyList()
    }

    @Test
    fun `aggregate when single report exists in DB returns aggregated report`() {
        val endDate = YearMonth.from(LocalDate.now())
        val startDate = endDate.minusMonths(1)
        val userId = 5.asId<User>()

        val expectedExpenses = Report(
            byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 10, 0))),
            total = listOf(PositiveMoney(Currency.EUR, 10, 0))
        )

        val expectedEarnings = Report(
            byCategory = mapOf(2.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 5, 0))),
            total = listOf(PositiveMoney(Currency.EUR, 5, 0))
        )

        val expectedTotal = listOf(Money(Currency.EUR, -5, 0))

        every { monthlyUserReportStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns listOf(
            monthlyUserReport(userId, startDate) {
                expenses = expectedExpenses
                earnings = expectedEarnings
                total = expectedTotal
            }
        )

        val result = monthlyReportAggregationImpl.makeReport(userId, startDate, endDate)

        result.userId shouldBe userId
        result.periods shouldContainExactlyInAnyOrder listOf(startDate)
        result.earnings shouldBe expectedEarnings
        result.expenses shouldBe expectedExpenses
        result.transfers shouldBe Report.empty()
        result.total shouldBe expectedTotal
    }

    @Test
    fun `aggregate when multiple reports exists in DB returns aggregated report`() {
        val endDate = YearMonth.from(LocalDate.now())
        val startDate = endDate.minusMonths(1)
        val userId = 5.asId<User>()

        val expectedEarnings = Report(
            byCategory = mapOf(2.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 5, 0))),
            total = listOf(PositiveMoney(Currency.EUR, 5, 0))
        )

        val expectedTransfers = Report(
            byCategory = mapOf(
                2.asId<Category>() to
                        listOf(Money(Currency.EUR, -9, 0), Money(Currency.USD, 11, 0))
            ),
            total = listOf(Money(Currency.EUR, -9, 0), Money(Currency.USD, 11, 0))
        )

        every { monthlyUserReportStorage.findAllByUserIdBetween(userId, startDate, endDate) } returns listOf(
            monthlyUserReport(userId, startDate) {
                expenses = Report(
                    byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.EUR, 10, 0))),
                    total = listOf(PositiveMoney(Currency.EUR, 10, 0))
                )
                earnings = expectedEarnings
                total = listOf(Money(Currency.EUR, -5, 0))
            },
            monthlyUserReport(userId, endDate) {
                expenses = Report(
                    byCategory = mapOf(1.asId<Category>() to listOf(PositiveMoney(Currency.USD, 10, 0))),
                    total = listOf(PositiveMoney(Currency.USD, 10, 0))
                )
                transfers = expectedTransfers
                total = listOf(Money(Currency.EUR, -9, 0), Money(Currency.USD, 1, 0))
            }
        )

        val result = monthlyReportAggregationImpl.makeReport(userId, startDate, endDate)

        result.userId shouldBe userId
        result.periods shouldContainExactlyInAnyOrder listOf(startDate, endDate)
        result.earnings shouldBe expectedEarnings
        result.expenses shouldBe Report(
            byCategory = mapOf(
                1.asId<Category>() to listOf(
                    PositiveMoney(Currency.EUR, 10, 0),
                    PositiveMoney(Currency.USD, 10, 0)
                )
            ),
            total = listOf(
                PositiveMoney(Currency.EUR, 10, 0),
                PositiveMoney(Currency.USD, 10, 0)
            )
        )
        result.transfers shouldBe expectedTransfers
        result.total shouldContainExactlyInAnyOrder listOf(
            Money(Currency.EUR, -14, 0),
            Money(Currency.USD, 1, 0)
        )
    }
}