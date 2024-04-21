package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase

@JsonInclude(JsonInclude.Include.NON_NULL)
internal data class TransferDto(
    val id: Long,
    val userId: Long,
    val date: String,
    val send: AccountMoneyDto?,
    val received: AccountMoneyDto,
    val description: String,
    val categoryId: Long,
)

internal fun <T : CommonTransferBase> T.toTransferDto() = TransferDto(
    id.value,
    userId.value,
    date.toString(),
    send?.toAccountsMoneyDto(),
    received.toAccountsMoneyDto(),
    description,
    categoryId.value,
)