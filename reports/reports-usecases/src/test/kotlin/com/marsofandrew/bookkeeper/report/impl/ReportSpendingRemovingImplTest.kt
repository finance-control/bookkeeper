package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.fixture.dailyUserReport
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
import com.marsofandrew.bookkeeper.report.fixture.spending
import com.marsofandrew.bookkeeper.report.fixture.yearlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReportSpendingRemovingImplTest {

    private val userId = 5.asId<User>()

    private val dailyUserReportStorage = mockk<DailyUserReportStorage>(relaxUnitFun = true)
    private val monthlyUserReportStorage = mockk<MonthlyUserReportStorage>(relaxUnitFun = true)
    private val yearlyUserReportStorage = mockk<YearlyUserReportStorage>(relaxUnitFun = true)

    private val transactionExecutor = object : TransactionExecutor {
        override fun <T> execute(block: () -> T): T = block()
    }

    private lateinit var reportSpendingRemovingImpl: ReportSpendingRemovingImpl

    @BeforeEach
    fun setup() {
        reportSpendingRemovingImpl = ReportSpendingRemovingImpl(
            dailyUserReportStorage,
            monthlyUserReportStorage,
            yearlyUserReportStorage,
            transactionExecutor
        )
    }

    @Test
    fun `remove throws exception when reports are absent`() {
        val now = LocalDate.now()
        val spending = spending(userId) {
            date = now
            money = PositiveMoney(Currency.EUR, 10, 0)
        }

        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null

        shouldThrowExactly<IllegalArgumentException> {
            reportSpendingRemovingImpl.remove(spending)
        }
    }

    @Test
    fun `remove when reports are present removes spending from report`() {
        val now = LocalDate.now()
        val spending = spending(userId) {
            date = now
            money = PositiveMoney(Currency.EUR, 10, 0)
        }

        val expectedExpensesReport = Report(
            byCategory = mapOf(spending.categoryId to listOf(spending.money)),
            total = listOf(spending.money)
        )

        val defaultEarningsReport = Report(
            byCategory = mapOf(5.asId<TransferCategory>() to listOf(PositiveMoney(Currency.EUR, 10))),
            total = listOf(PositiveMoney(Currency.EUR, 9, 0))
        )


        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns dailyUserReport(userId, now) {
            earnings = defaultEarningsReport
            expenses = expectedExpensesReport
            total = listOf(Money(Currency.EUR, -1, 0))
        }
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns monthlyUserReport(
            userId,
            YearMonth.from(now)
        ) {
            earnings = defaultEarningsReport
            expenses = expectedExpensesReport
            total = listOf(Money(Currency.EUR, -1, 0))
        }
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns yearlyUserReport(
            userId,
            Year.from(now)
        ) {
            earnings = defaultEarningsReport
            expenses = expectedExpensesReport
            total = listOf(Money(Currency.EUR, -1, 0))
        }

        reportSpendingRemovingImpl.remove(spending)

        verify(exactly = 1) {
            dailyUserReportStorage.createOrUpdate(dailyUserReport(userId, now) {
                earnings = defaultEarningsReport
                total = listOf(Money(Currency.EUR, 9, 0))
            })
        }

        verify(exactly = 1) {
            monthlyUserReportStorage.createOrUpdate(monthlyUserReport(userId, YearMonth.from(now)) {
                earnings = defaultEarningsReport
                total = listOf(Money(Currency.EUR, 9, 0))
            }
            )
        }

        verify(exactly = 1) {
            yearlyUserReportStorage.createOrUpdate(yearlyUserReport(userId, Year.from(now)) {
                earnings = defaultEarningsReport
                total = listOf(Money(Currency.EUR, 9, 0))
            })
        }
    }
}