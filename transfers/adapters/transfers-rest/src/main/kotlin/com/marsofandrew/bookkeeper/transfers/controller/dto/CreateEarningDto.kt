package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

internal data class CreateEarningDto(
    val date: LocalDate,
    val received: AccountMoneyDto,
    val description: String,
    val categoryId: Long,
) {
    fun toEarning(userId: NumericId<User>, clock: Clock) = Earning(
        id = NumericId.unidentified(),
        userId = userId,
        date = date,
        received = received.toAccountMoney(),
        description = description,
        categoryId = categoryId.asId(),
        createdAt = LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
        version = Version(0)
    )
}
