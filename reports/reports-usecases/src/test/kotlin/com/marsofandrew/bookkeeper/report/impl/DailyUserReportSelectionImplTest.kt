package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.fixture.dailyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DailyUserReportSelectionImplTest {

    private val userId = 5.asId<User>()
    private val dailyUserReportStorage = mockk<DailyUserReportStorage>()

    private lateinit var dailyReportSelectionImpl: DailyUserReportSelectionImpl

    @BeforeEach
    fun setup() {
        dailyReportSelectionImpl = DailyUserReportSelectionImpl(dailyUserReportStorage)
    }

    @Test
    fun `select throws exception when startDate is greater than endDate`() {
        val now = LocalDate.now()
        shouldThrowExactly<IllegalArgumentException> {
            dailyReportSelectionImpl.select(userId, now, now.minusDays(2))
        }
    }

    @Test
    fun `select when reports is absent in db returns empty list`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(2)

        every { dailyUserReportStorage.findAllByUserIdBetween(userId, startDate, now) } returns emptyList()

        val result = dailyReportSelectionImpl.select(userId, startDate, now)

        result shouldHaveSize 0
    }

    @Test
    fun `select when reports are present in db returns those reports`() {
        val now = LocalDate.now()
        val startDate = now.minusDays(2)
        val reports = listOf(
            dailyUserReport(userId, startDate),
            dailyUserReport(userId, now)
        )

        every { dailyUserReportStorage.findAllByUserIdBetween(userId, startDate, now) } returns reports

        val result = dailyReportSelectionImpl.select(userId, startDate, now)

        result shouldContainExactlyInAnyOrder reports
    }

}