package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

internal data class CreateSpendingDto(
    val money: AccountBoundedMoney,
    val date: LocalDate,
    val comment: String,
    val spendingCategoryId: Long,
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Spending(
        NumericId.unidentified(),
        userId,
        money.money.toPositiveMoney(),
        date,
        comment,
        spendingCategoryId.asId(),
        clock.date(),
        money.accountId?.asId()
    )
}