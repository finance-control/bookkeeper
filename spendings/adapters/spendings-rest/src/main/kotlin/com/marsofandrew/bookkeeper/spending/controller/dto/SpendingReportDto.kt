package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.SpendingReport

internal data class SpendingReportDto(
    val spendingByCategory: Map<Long, List<PositiveMoneyDto>>,
    val total: List<PositiveMoneyDto>
)

internal fun SpendingReport.toSpendingReportDto() = SpendingReportDto(
    spendingByCategory = spendingByCategory.entries
        .associate { (category, data) ->
            category.value to data.map { it.toPositiveMoneyDto() }
        },
    total = total.map { it.toPositiveMoneyDto() }
)
