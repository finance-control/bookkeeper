package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.spendings.Spending

internal data class SpendingDto(
    val id: Long,
    val userId: Long,
    val money: AccountBoundedMoney,
    val date: String,
    val comment: String,
    val spendingCategoryId: Long,
) {

}

internal fun Spending.toSpendingDto() = SpendingDto(
    id.value,
    userId.value,
    AccountBoundedMoney(money.toPositiveMoneyDto(), fromAccount?.value),
    date.toString(),
    comment,
    spendingCategoryId.value,
)