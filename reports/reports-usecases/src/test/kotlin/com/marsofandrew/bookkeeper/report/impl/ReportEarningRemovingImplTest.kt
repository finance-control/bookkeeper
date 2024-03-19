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
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.fixture.dailyUserReport
import com.marsofandrew.bookkeeper.report.fixture.earning
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
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

internal class ReportEarningRemovingImplTest {

    private val userId = 5.asId<User>()

    private val dailyUserReportStorage = mockk<DailyUserReportStorage>(relaxUnitFun = true)
    private val monthlyUserReportStorage = mockk<MonthlyUserReportStorage>(relaxUnitFun = true)
    private val yearlyUserReportStorage = mockk<YearlyUserReportStorage>(relaxUnitFun = true)

    private val transactionExecutor = object : TransactionExecutor {
        override fun <T> execute(block: () -> T): T = block()
    }

    private lateinit var reportEarningRemovingImpl: ReportEarningRemovingImpl

    @BeforeEach
    fun setup() {
        reportEarningRemovingImpl = ReportEarningRemovingImpl(
            dailyUserReportStorage,
            monthlyUserReportStorage,
            yearlyUserReportStorage,
            transactionExecutor
        )
    }

    @Test
    fun `remove throws exception when reports are absent`() {
        val now = LocalDate.now()
        val earning = earning(userId) {
            date = now
            money = PositiveMoney(Currency.EUR, 10, 0)
        }

        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null

        shouldThrowExactly<IllegalArgumentException> {
            reportEarningRemovingImpl.remove(earning)
        }
    }

    @Test
    fun `remove when reports are present removes earning from report`() {
        val now = LocalDate.now()
        val earning = earning(userId) {
            date = now
            money = PositiveMoney(Currency.EUR, 10, 0)
        }

        val expectedEarningsReport = Report(
            byCategory = mapOf(earning.categoryId to listOf(earning.money)),
            total = listOf(earning.money)
        )

        val defaultExpensesReport = Report(
            byCategory = mapOf(5.asId<SpendingCategory>() to listOf(PositiveMoney(Currency.EUR, 9, 0))),
            total = listOf(PositiveMoney(Currency.EUR, 9, 0))
        )


        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns dailyUserReport(userId, now) {
            earnings = expectedEarningsReport
            expenses = defaultExpensesReport
            total = listOf(Money(Currency.EUR, 1, 0))
        }
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns monthlyUserReport(
            userId,
            YearMonth.from(now)
        ) {
            earnings = expectedEarningsReport
            expenses = defaultExpensesReport
            total = listOf(Money(Currency.EUR, 1, 0))
        }
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns yearlyUserReport(
            userId,
            Year.from(now)
        ) {
            earnings = expectedEarningsReport
            expenses = defaultExpensesReport
            total = listOf(Money(Currency.EUR, 1, 0))
        }

        reportEarningRemovingImpl.remove(earning)

        verify(exactly = 1) {
            dailyUserReportStorage.createOrUpdate(dailyUserReport(userId, now) {
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -9, 0))
            })
        }

        verify(exactly = 1) {
            monthlyUserReportStorage.createOrUpdate(monthlyUserReport(userId, YearMonth.from(now)) {
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -9, 0))
            })
        }

        verify(exactly = 1) {
            yearlyUserReportStorage.createOrUpdate(yearlyUserReport(userId, Year.from(now)) {
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -9, 0))
            })
        }
    }
}