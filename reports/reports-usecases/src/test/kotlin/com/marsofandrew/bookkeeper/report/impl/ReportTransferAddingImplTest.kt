package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
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
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
import com.marsofandrew.bookkeeper.report.fixture.transfer
import com.marsofandrew.bookkeeper.report.fixture.yearlyUserReport
import com.marsofandrew.bookkeeper.report.impl.util.toMoney
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.user.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReportTransferAddingImplTest {

    private val userId = 5.asId<User>()

    private val dailyUserReportStorage = mockk<DailyUserReportStorage>(relaxUnitFun = true)
    private val monthlyUserReportStorage = mockk<MonthlyUserReportStorage>(relaxUnitFun = true)
    private val yearlyUserReportStorage = mockk<YearlyUserReportStorage>(relaxUnitFun = true)

    private val transactionalExecution = object : TransactionalExecution {
        override fun <T> execute(block: () -> T): T = block()
    }

    private lateinit var reportTransferAddingImpl: ReportTransferAddingImpl

    @BeforeEach
    fun setup() {
        reportTransferAddingImpl = ReportTransferAddingImpl(
            dailyUserReportStorage,
            monthlyUserReportStorage,
            yearlyUserReportStorage,
            transactionalExecution
        )
    }

    @Test
    fun `add when reports are absent then creates reports from that earning`() {
        val now = LocalDate.now()
        val transfer = transfer(userId) {
            date = now
            send = PositiveMoney(Currency.EUR, 10, 0)
            received = PositiveMoney(Currency.USD, 11, 0)
        }

        val expectedTransfersReport = Report(
            byCategory = mapOf(transfer.transferCategoryId to listOf(transfer.received.toMoney(), -transfer.send)),
            total = listOf(transfer.received.toMoney(), -transfer.send)
        )

        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns null

        reportTransferAddingImpl.add(transfer)

        verify(exactly = 1) {
            dailyUserReportStorage.createOrUpdate(dailyUserReport(userId, now) {
                transfers = expectedTransfersReport
                total = expectedTransfersReport.total
            })
        }

        verify(exactly = 1) {
            monthlyUserReportStorage.createOrUpdate(monthlyUserReport(userId, YearMonth.from(now)) {
                transfers = expectedTransfersReport
                total = expectedTransfersReport.total
            })
        }

        verify(exactly = 1) {
            yearlyUserReportStorage.createOrUpdate(yearlyUserReport(userId, Year.from(now)) {
                transfers = expectedTransfersReport
                total = expectedTransfersReport.total
            })
        }
    }

    @Test
    fun `add when reports exist in DB then adds that spending to each corresponded report`() {
        val now = LocalDate.now()
        val transfer = transfer(userId) {
            date = now
            send = PositiveMoney(Currency.EUR, 10, 0)
            received = PositiveMoney(Currency.USD, 11, 0)
        }

        val expectedTransfersReport = Report(
            byCategory = mapOf(transfer.transferCategoryId to listOf(transfer.received.toMoney(), -transfer.send)),
            total = listOf(transfer.received.toMoney(), -transfer.send)
        )

        val defaultExpensesReport = Report(
            byCategory = mapOf(5.asId<SpendingCategory>() to listOf(PositiveMoney(Currency.EUR, 10))),
            total = listOf(PositiveMoney(Currency.EUR, 9, 0))
        )


        every { dailyUserReportStorage.findByUserIdAndDate(userId, now) } returns dailyUserReport(userId, now) {
            expenses = defaultExpensesReport
            total = defaultExpensesReport.total.map { -it }
        }
        every { monthlyUserReportStorage.findByUserIdAndDate(userId, now) } returns monthlyUserReport(
            userId,
            YearMonth.from(now)
        ) {
            expenses = defaultExpensesReport
            total = defaultExpensesReport.total.map { -it }
        }
        every { yearlyUserReportStorage.findByUserIdAndDate(userId, now) } returns yearlyUserReport(
            userId,
            Year.from(now)
        ) {
            expenses = defaultExpensesReport
            total = defaultExpensesReport.total.map { -it }
        }

        reportTransferAddingImpl.add(transfer)

        verify(exactly = 1) {
            dailyUserReportStorage.createOrUpdate(dailyUserReport(userId, now) {
                transfers = expectedTransfersReport
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -19, 0), Money(Currency.USD, 11, 0))
            })
        }

        verify(exactly = 1) {
            monthlyUserReportStorage.createOrUpdate(monthlyUserReport(userId, YearMonth.from(now)) {
                transfers = expectedTransfersReport
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -19, 0), Money(Currency.USD, 11, 0))
            })
        }

        verify(exactly = 1) {
            yearlyUserReportStorage.createOrUpdate(yearlyUserReport(userId, Year.from(now)) {
                transfers = expectedTransfersReport
                expenses = defaultExpensesReport
                total = listOf(Money(Currency.EUR, -19, 0), Money(Currency.USD, 11, 0))
            })
        }
    }
}