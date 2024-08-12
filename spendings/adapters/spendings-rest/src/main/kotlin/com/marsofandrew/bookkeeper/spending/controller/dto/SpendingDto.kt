package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.Spending

internal data class SpendingDto(
    val id: Long,
    val userId: Long,
    val money: AccountBoundedMoneyDto,
    val date: String,
    val description: String,
    val categoryId: Long,
    val version: Int
)

internal fun Spending.toSpendingDto() = SpendingDto(
    id = id.value,
    userId = userId.value,
    money = AccountBoundedMoneyDto(money.toPositiveMoneyDto(), sourceAccountId?.value),
    date = date.toString(),
    description = description,
    categoryId = categoryId.value,
    version = version.value
)
