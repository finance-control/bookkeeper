package com.marsofandrew.bookkeeper.transfer.controller.dto

import com.marsofandrew.bookkeeper.transfer.Transfer

internal data class TransferDto(
    val id: Long,
    val userId: Long,
    val date: String,
    val send: AccountMoneyDto?,
    val received: AccountMoneyDto,
    val description: String,
    val categoryId: Long,
)

internal fun Transfer.toTransferDto() = TransferDto(
    id.value,
    userId.value,
    date.toString(),
    send?.toAccountsMoneyDto(),
    received.toAccountsMoneyDto(),
    description,
    categoryId.value,
)