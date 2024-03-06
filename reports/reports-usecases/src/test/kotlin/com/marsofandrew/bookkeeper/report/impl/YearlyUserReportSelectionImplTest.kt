package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.fixture.yearlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import java.time.Year
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class YearlyUserReportSelectionImplTest {

    private val userId = 5.asId<User>()
    private val yearlyUserReportStorage = mockk<YearlyUserReportStorage>()

    private lateinit var yearlyReportSelectionImpl: YearlyUserReportSelectionImpl

    @BeforeEach
    fun setup() {
        yearlyReportSelectionImpl = YearlyUserReportSelectionImpl(yearlyUserReportStorage)
    }

    @Test
    fun `select when reports is absent in db returns empty list`() {
        every { yearlyUserReportStorage.findAllByUserId(userId) } returns emptyList()

        val result = yearlyReportSelectionImpl.select(userId)

        result shouldHaveSize 0
    }

    @Test
    fun `select when reports are present in db returns those reports`() {
        val reports = listOf(
            yearlyUserReport(userId, Year.now()),
        )

        every { yearlyUserReportStorage.findAllByUserId(userId) } returns reports

        val result = yearlyReportSelectionImpl.select(userId)

        result shouldContainExactlyInAnyOrder reports
    }

}