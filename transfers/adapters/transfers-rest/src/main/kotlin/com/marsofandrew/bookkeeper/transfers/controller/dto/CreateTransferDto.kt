package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

data class CreateTransferDto(
    val date: LocalDate,
    val send: PositiveMoneyDto? = null,
    val received: PositiveMoneyDto,
    val comment: String,
    val transferCategoryId: Long,
    val fee: PositiveMoneyDto? = null
) {
    fun toSpending(userId: NumericId<User>, clock: Clock) = Transfer(
        StringId.unidentified(),
        userId,
        date,
        send?.toPositiveMoney(),
        received.toPositiveMoney(),
        comment,
        transferCategoryId.asId(),
        LocalDate.ofInstant(clock.instant(), ZoneId.of("Z")),
        fee?.toPositiveMoney()
    )
}