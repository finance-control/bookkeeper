package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

internal data class CreateTransferDto(
    val date: LocalDate,
    val send: AccountMoneyDto,
    val received: AccountMoneyDto,
    val description: String,
    val categoryId: Long,
) {
    fun toTransfer(userId: NumericId<User>, clock: Clock) = Transfer(
        id = NumericId.unidentified(),
        userId = userId,
        date = date,
        send = send.toAccountMoney(),
        received = received.toAccountMoney(),
        description = description,
        categoryId = categoryId.asId(),
        createdAt = LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
        version = Version(0)
    )
}
