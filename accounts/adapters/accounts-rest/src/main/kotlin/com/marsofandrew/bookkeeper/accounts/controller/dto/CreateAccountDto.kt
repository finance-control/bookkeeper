package com.marsofandrew.bookkeeper.accounts.controller.dto

import com.marsofandrew.bookkeeper.accounts.Account
import com.marsofandrew.bookkeeper.accounts.user.User
import com.marsofandrew.bookkeeper.base.date
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import java.time.Clock
import java.time.LocalDate

internal data class CreateAccountDto(
    val money: MoneyDto,
    val title: String,
    val openedAt: LocalDate?,
) {
    fun toAccount(userId: NumericId<User>, clock: Clock) = Account(
        id = StringId.unidentified(),
        userId = userId,
        money = money.toMoney(),
        title = title,
        openedAt = openedAt ?: clock.date(),
        closedAt = null,
        status = Account.Status.IN_USE
    )
}
