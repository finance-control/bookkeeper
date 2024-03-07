package com.marsofandrew.bookkeeper.report.controller.dto

import com.marsofandrew.bookkeeper.report.Report

internal data class ReportDto(
    val byCategory: Map<Long, List<MoneyDto>>,
    val total: List<MoneyDto>
)

internal fun Report<*, *>.toReportDto() = ReportDto(
    byCategory =
    byCategory.entries.associate { (category, moneys) -> category.value to moneys.map { it.toMoneyDto() } },
    total = total.map { it.toMoneyDto() }
)
