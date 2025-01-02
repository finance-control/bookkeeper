package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingWithCategory
import io.swagger.v3.oas.annotations.media.Schema

internal data class SpendingDto(
    val id: Long,
    val userId: Long,
    val money: AccountBoundedMoneyDto,
    val date: String,
    val description: String,
    @Deprecated(message = "Moved into category", replaceWith = ReplaceWith("category.id"))
    @Schema(deprecated = true)
    val categoryId: Long,
    val version: Int,
    val category: CategoryDto
)

internal fun SpendingWithCategory.toSpendingDto() = SpendingDto(
    id = spending.id.value,
    userId = spending.userId.value,
    money = AccountBoundedMoneyDto(spending.money.toPositiveMoneyDto(), spending.sourceAccountId?.value),
    date = spending.date.toString(),
    description = spending.description,
    categoryId = spending.categoryId.value,
    version = spending.version.value,
    category = category.toCategoryDto()
)
