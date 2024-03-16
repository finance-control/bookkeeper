package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.base.model.Version
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
        id = NumericId.unidentified(),
        userId = userId,
        money = money.money.toPositiveMoney(),
        date = date,
        description = description,
        spendingCategoryId = spendingCategoryId.asId(),
        createdAt = clock.date(),
        fromAccount = money.accountId?.asId(),
        version = Version(0)
    )
}
