package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

internal data class CreateTransferDto(
    val date: LocalDate,
    val send: AccountMoneyDto? = null,
    val received: AccountMoneyDto,
    val comment: String,
    val transferCategoryId: Long,
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Transfer(
        NumericId.unidentified(),
        userId,
        date,
        send?.toAccountMoney(),
        received.toAccountMoney(),
        comment,
        transferCategoryId.asId(),
        LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
    )
}