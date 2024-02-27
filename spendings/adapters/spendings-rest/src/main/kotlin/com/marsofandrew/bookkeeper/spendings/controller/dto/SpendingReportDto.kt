package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.spendings.SpendingReport

data class SpendingReportDto(
    val spendingByCategory: Map<Long, List<PositiveMoneyDto>>,
    val total: List<PositiveMoneyDto>
)

fun SpendingReport.toSpendingReportDto() = SpendingReportDto(
    spendingByCategory = spendingByCategory.entries
        .associate { (category, data) ->
            category.value to data.map { it.toPositiveMoneyDto() }
        },
    total = total.map { it.toPositiveMoneyDto() }
)
