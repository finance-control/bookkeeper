package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.SpendingReportsWithCategories

internal data class SpendingReportDto(
    val spendingByCategory: Map<Long, List<PositiveMoneyDto>>,
    val total: List<PositiveMoneyDto>,
    val categoryById: Map<Long, CategoryDto>
)

internal fun SpendingReportsWithCategories.toSpendingReportDto() = SpendingReportDto(
    spendingByCategory = report.spendingByCategory.entries
        .associate { (category, data) ->
            category.value to data.map { it.toPositiveMoneyDto() }
        },
    total = report.total.map { it.toPositiveMoneyDto() },
    categoryById = categories.map { it.key.value to it.value.toCategoryDto() }
        .toMap()
)
