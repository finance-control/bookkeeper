package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MonthlyUserReportSelectionImplTest {

    private val userId = 5.asId<User>()
    private val monthlyUserReportStorage = mockk<MonthlyUserReportStorage>()

    private lateinit var monthlyReportSelectionImpl: MonthlyUserReportSelectionImpl

    @BeforeEach
    fun setup() {
        monthlyReportSelectionImpl = MonthlyUserReportSelectionImpl(monthlyUserReportStorage)
    }

    @Test
    fun `select throws exception when startMonth is greater than endMonth`() {
        val now = YearMonth.from(LocalDate.now())
        shouldThrowExactly<IllegalArgumentException> {
            monthlyReportSelectionImpl.select(userId, now, now.minusMonths(2))
        }
    }

    @Test
    fun `select when reports is absent in db returns empty list`() {
        val now = YearMonth.from(LocalDate.now())
        val startMonth = now.minusMonths(2)

        every { monthlyUserReportStorage.findAllByUserIdBetween(userId, startMonth, now) } returns emptyList()

        val result = monthlyReportSelectionImpl.select(userId, startMonth, now)

        result shouldHaveSize 0
    }

    @Test
    fun `select when reports are present in db returns those reports`() {
        val now = YearMonth.from(LocalDate.now())
        val startMonth = now.minusMonths(2)
        val reports = listOf(
            monthlyUserReport(userId, startMonth),
            monthlyUserReport(userId, now)
        )

        every { monthlyUserReportStorage.findAllByUserIdBetween(userId, startMonth, now) } returns reports

        val result = monthlyReportSelectionImpl.select(userId, startMonth, now)

        result shouldContainExactlyInAnyOrder reports
    }

}