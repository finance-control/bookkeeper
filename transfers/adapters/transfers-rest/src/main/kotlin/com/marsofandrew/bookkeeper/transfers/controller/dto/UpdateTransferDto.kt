package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.TransferUpdate
import java.time.LocalDate

internal data class UpdateTransferDto(
    val date: LocalDate?,
    val send: AccountMoneyDto?,
    val received: AccountMoneyDto?,
    val description: String?,
    val categoryId: Long?,
    val version: Int
)

internal fun UpdateTransferDto.toTransferUpdate(id: NumericId<CommonTransfer>) = TransferUpdate(
    id = id,
    date = date,
    send = send?.toAccountMoney(),
    received = received?.toAccountMoney(),
    description = description,
    categoryId = categoryId?.asId(),
    version = Version(version)
)
