package com.marsofandrew.bookkeeper.report.controller

import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.DailyUserReportAggregation
import com.marsofandrew.bookkeeper.report.DailyUserReportSelection
import com.marsofandrew.bookkeeper.report.MonthlyUserReportAggregation
import com.marsofandrew.bookkeeper.report.MonthlyUserReportSelection
import com.marsofandrew.bookkeeper.report.YearlyUserReportSelection
import com.marsofandrew.bookkeeper.report.controller.dto.AggregatedReportDto
import com.marsofandrew.bookkeeper.report.controller.dto.UserReportDto
import com.marsofandrew.bookkeeper.report.controller.dto.toAggregatedReportDto
import com.marsofandrew.bookkeeper.report.controller.dto.toUserReportDto
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import java.time.LocalDate
import java.time.YearMonth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reports")
internal class ReportsController(
    private val dailyUserReportSelection: DailyUserReportSelection,
    private val monthlyUserReportSelection: MonthlyUserReportSelection,
    private val yearlyUserReportSelection: YearlyUserReportSelection,
    private val dailyUserReportAggregation: DailyUserReportAggregation,
    private val monthlyUserReportAggregation: MonthlyUserReportAggregation
) {

    @GetMapping("/daily")
    fun selectDailyReports(
        @Parameter(hidden = true) @UserId userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<UserReportDto> {
        return dailyUserReportSelection.select(userId.asId(), startDate, endDate)
            .map { it.toUserReportDto { date.toString() } }
    }

    @GetMapping("/monthly")
    fun selectMonthlyReports(
        @Parameter(hidden = true) @UserId userId: Long,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): List<UserReportDto> {
        return monthlyUserReportSelection.select(userId.asId(), startMonth, endMonth)
            .map { it.toUserReportDto { month.toString() } }
    }

    @GetMapping("/yearly")
    fun selectYearlyReports(
        @Parameter(hidden = true) @UserId userId: Long,
    ): List<UserReportDto> {
        return yearlyUserReportSelection.select(userId.asId())
            .map { it.toUserReportDto { year.toString() } }
    }

    @GetMapping("/daily/aggregation")
    fun aggregateDaily(
        @Parameter(hidden = true) @UserId userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): AggregatedReportDto {
        return dailyUserReportAggregation.makeReport(userId.asId(), startDate, endDate).toAggregatedReportDto()
    }

    @GetMapping("/monthly/aggregation")
    fun aggregateMonthly(
        @Parameter(hidden = true) @UserId userId: Long,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): AggregatedReportDto {
        return monthlyUserReportAggregation.makeReport(userId.asId(), startMonth, endMonth).toAggregatedReportDto()
    }
}
