package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.Transfer

internal data class TransferDto(
    val id: Long,
    val userId: Long,
    val date: String,
    val send: AccountMoneyDto?,
    val received: AccountMoneyDto,
    val comment: String,
    val transferCategoryId: Long,
)

internal fun Transfer.toTransferDto() = TransferDto(
    id.value,
    userId.value,
    date.toString(),
    send?.toAccountsMoneyDto(),
    received.toAccountsMoneyDto(),
    comment,
    transferCategoryId.value,
)