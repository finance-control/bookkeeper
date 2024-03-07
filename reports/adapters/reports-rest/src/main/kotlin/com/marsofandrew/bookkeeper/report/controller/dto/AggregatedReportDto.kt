package com.marsofandrew.bookkeeper.report.controller.dto

import com.marsofandrew.bookkeeper.report.AggregatedUserReport

internal data class AggregatedReportDto(
    val userId: Long,
    val periods: List<String>,
    val expenses: ReportDto,
    val earnings: ReportDto,
    val transfers: ReportDto,
    val total: List<MoneyDto>
)

internal fun AggregatedUserReport<*>.toAggregatedReportDto() = AggregatedReportDto(
    userId = userId.value,
    periods = periods.map { it.toString() },
    expenses = expenses.toReportDto(),
    earnings = earnings.toReportDto(),
    transfers = transfers.toReportDto(),
    total = total.map { it.toMoneyDto() }
)
