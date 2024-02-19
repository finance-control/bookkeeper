package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

data class CreateSpendingDto(
    val money: PositiveMoneyDto,
    val date: LocalDate,
    val comment: String,
    val spendingCategoryId: String
) {
    fun toSpending(userId: NumericId<User>) = Spending(
        StringId.unidentified(),
        userId,
        money.toPositiveMoney(),
        date,
        comment,
        spendingCategoryId.asId()
    )
}