package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.Clock
import java.time.LocalDate

internal data class CreateSpendingDto(
    val money: AccountBoundedMoneyDto,
    val date: LocalDate,
    val description: String,
    val spendingCategoryId: Long,
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Spending(
        NumericId.unidentified(),
        userId,
        money.money.toPositiveMoney(),
        date,
        description,
        spendingCategoryId.asId(),
        clock.date(),
        money.accountId?.asId()
    )
}