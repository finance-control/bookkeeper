package com.marsofandrew.bookkeeper.spendings.controller.dto

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

data class CreateSpendingDto(
    val money: PositiveMoneyDto,
    val date: LocalDate,
    val comment: String,
    val spendingCategoryId: Long
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Spending(
        StringId.unidentified(),
        userId,
        money.toPositiveMoney(),
        date,
        comment,
        spendingCategoryId.asId(),
        LocalDate.ofInstant(clock.instant(), ZoneId.of("Z"))
    )
}