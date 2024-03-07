package com.marsofandrew.bookkeeper.report.controller.dto

import com.marsofandrew.bookkeeper.report.BaseUserReport

internal data class UserReportDto(
    val userId: Long,
    val period: String,
    val expenses: ReportDto,
    val earnings: ReportDto,
    val transfers: ReportDto,
    val total: List<MoneyDto>
)

internal fun <T : BaseUserReport> T.toUserReportDto(period: T.() -> String) = UserReportDto(
    userId = userId.value,
    period = period(),
    expenses = expenses.toReportDto(),
    earnings = earnings.toReportDto(),
    transfers = transfers.toReportDto(),
    total = total.map { it.toMoneyDto() }
)
