package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.transfers.Transfer

import java.time.LocalDate

data class TransferDto(
    val id: String,
    val userId: Long,
    val date: LocalDate,
    val send: PositiveMoneyDto?,
    val received: PositiveMoneyDto,
    val comment: String,
    val transferCategoryId: String,
    val fee: PositiveMoneyDto? = null
)

fun Transfer.toTransferDto() = TransferDto(
    id.value,
    userId.value,
    date,
    send?.toPositiveMoneyDto(),
    received.toPositiveMoneyDto(),
    comment,
    transferCategoryId.value,
    fee?.toPositiveMoneyDto()
)