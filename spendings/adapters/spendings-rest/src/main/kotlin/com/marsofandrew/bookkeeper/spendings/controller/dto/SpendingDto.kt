package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.spendings.Spending
import java.time.LocalDate

data class SpendingDto(
    val id: String,
    val userId: Long,
    val money: PositiveMoneyDto,
    val date: LocalDate,
    val comment: String,
    val spendingCategoryId: String
)

fun Spending.toSpendingDto() = SpendingDto(
    id.value,
    userId.value,
    money.toPositiveMoneyDto(),
    date,
    comment,
    spendingCategoryId.value
)