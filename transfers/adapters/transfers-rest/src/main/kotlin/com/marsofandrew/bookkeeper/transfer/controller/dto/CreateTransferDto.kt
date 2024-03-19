package com.marsofandrew.bookkeeper.transfer.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

internal data class CreateTransferDto(
    val date: LocalDate,
    val send: AccountMoneyDto? = null,
    val received: AccountMoneyDto,
    val description: String,
    val transferCategoryId: Long,
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Transfer(
        id = NumericId.unidentified(),
        userId = userId,
        date = date,
        send = send?.toAccountMoney(),
        received = received.toAccountMoney(),
        description = description,
        transferCategoryId = transferCategoryId.asId(),
        createdAt = LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
        version = Version(0)
    )
}
