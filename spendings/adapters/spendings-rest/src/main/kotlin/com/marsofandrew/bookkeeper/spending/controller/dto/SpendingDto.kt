package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.spending.Spending

internal data class SpendingDto(
    val id: Long,
    val userId: Long,
    val money: AccountBoundedMoneyDto,
    val date: String,
    val description: String,
    val spendingCategoryId: Long,
) {

}

internal fun Spending.toSpendingDto() = SpendingDto(
    id.value,
    userId.value,
    AccountBoundedMoneyDto(money.toPositiveMoneyDto(), fromAccount?.value),
    date.toString(),
    description,
    spendingCategoryId.value,
)