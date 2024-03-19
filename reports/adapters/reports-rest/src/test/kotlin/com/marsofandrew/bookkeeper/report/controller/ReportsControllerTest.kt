package com.marsofandrew.bookkeeper.report.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.DailyUserReportAggregation
import com.marsofandrew.bookkeeper.report.DailyUserReportSelection
import com.marsofandrew.bookkeeper.report.MonthlyUserReportAggregation
import com.marsofandrew.bookkeeper.report.MonthlyUserReportSelection
import com.marsofandrew.bookkeeper.report.YearlyUserReportSelection
import com.marsofandrew.bookkeeper.report.fixture.aggregatedReport
import com.marsofandrew.bookkeeper.report.fixture.dailyUserReport
import com.marsofandrew.bookkeeper.report.fixture.monthlyUserReport
import com.marsofandrew.bookkeeper.report.fixture.yearlyUserReport
import com.marsofandrew.bookkeeper.userContext.AuthArgumentContextConfiguration
import com.marsofandrew.bookkeeper.userContext.UserIdToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(
    classes = [
        AuthArgumentContextConfiguration::class,
        ReportsControllerTest.ContextConfiguration::class,
    ]
)
internal class ReportsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        clearAllMocks()
        SecurityContextHolder.getContext().authentication = UserIdToken(USER_ID)
    }

    @Test
    fun `select daily returns selected daily reports`() {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(5)

        val reports = listOf(dailyUserReport(USER_ID.asId(), startDate))

        every { dailyUserReportSelection.select(USER_ID.asId(), startDate, endDate) } returns reports

        mockMvc.get("/api/v1/reports/daily?startDate=$startDate&endDate=$endDate")
            .andExpect {
                status { isOk() }
                jsonPath("[0].userId") { value(reports[0].userId.value) }
                jsonPath("[0].period") { value(reports[0].date.toString()) }
            }
    }

    @Test
    fun `select monthly returns selected daily reports`() {
        val endMonth = YearMonth.now()
        val startMonth = endMonth.minusMonths(1)

        val reports = listOf(monthlyUserReport(USER_ID.asId(), startMonth))

        every { monthlyUserReportSelection.select(USER_ID.asId(), startMonth, endMonth) } returns reports

        mockMvc.get("/api/v1/reports/monthly?startMonth=$startMonth&endMonth=$endMonth")
            .andExpect {
                status { isOk() }
                jsonPath("[0].userId") { value(reports[0].userId.value) }
                jsonPath("[0].period") { value(reports[0].month.toString()) }
            }
    }

    @Test
    fun `select yearly returns selected daily reports`() {
        val reports = listOf(yearlyUserReport(USER_ID.asId(), Year.now()))

        every { yearlyUserReportSelection.select(USER_ID.asId()) } returns reports

        mockMvc.get("/api/v1/reports/yearly")
            .andExpect {
                status { isOk() }
                jsonPath("[0].userId") { value(reports[0].userId.value) }
                jsonPath("[0].period") { value(reports[0].year.toString()) }
            }
    }

    @Test
    fun `aggregate daily user reports`() {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(5)

        val report = aggregatedReport(USER_ID.asId(), listOf(endDate, startDate))

        every { dailyUserReportAggregation.makeReport(USER_ID.asId(), startDate, endDate) } returns report

        mockMvc.get("/api/v1/reports/daily/aggregation?startDate=$startDate&endDate=$endDate")
            .andExpect {
                status { isOk() }
                jsonPath("userId") { value(USER_ID) }
                jsonPath("periods[0]") { value(report.periods[0].toString()) }
                jsonPath("periods[1]") { value(report.periods[1].toString()) }
            }
    }

    @Test
    fun `aggregate monthly user reports`() {
        val endMonth = YearMonth.now()
        val startMonth = endMonth.minusMonths(5)

        val report = aggregatedReport(USER_ID.asId(), listOf(endMonth, startMonth))

        every { monthlyUserReportAggregation.makeReport(USER_ID.asId(), startMonth, endMonth) } returns report

        mockMvc.get("/api/v1/reports/monthly/aggregation?startMonth=$startMonth&endMonth=$endMonth")
            .andExpect {
                status { isOk() }
                jsonPath("userId") { value(USER_ID) }
                jsonPath("periods[0]") { value(report.periods[0].toString()) }
                jsonPath("periods[1]") { value(report.periods[1].toString()) }
            }
    }

    @Configuration
    class ContextConfiguration {

        @Bean
        fun reportsController() = ReportsController(
            dailyUserReportSelection,
            monthlyUserReportSelection,
            yearlyUserReportSelection,
            dailyUserReportAggregation,
            monthlyUserReportAggregation
        )
    }

    companion object {
        const val USER_ID = 4587L

        val dailyUserReportSelection = mockk<DailyUserReportSelection>()
        val monthlyUserReportSelection = mockk<MonthlyUserReportSelection>()
        val yearlyUserReportSelection = mockk<YearlyUserReportSelection>()
        val dailyUserReportAggregation = mockk<DailyUserReportAggregation>()
        val monthlyUserReportAggregation = mockk<MonthlyUserReportAggregation>()
    }
}
