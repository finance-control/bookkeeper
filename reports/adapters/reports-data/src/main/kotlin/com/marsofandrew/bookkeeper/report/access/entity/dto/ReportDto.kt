package com.marsofandrew.bookkeeper.report.access.entity.dto

import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.id.asId
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

internal fun <Category, M : BaseMoney> ReportDto.toReport(converter: Money.() -> M) = Report<Category, M>(
    byCategory = byCategory.entries.associate { (category, moneys) ->
        category.asId<Category>() to moneys.map { it.toMoney().converter() }
    },
    total = total.map { it.toMoney().converter() }
)