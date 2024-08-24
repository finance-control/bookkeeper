package com.marsofandrew.bookkeeper.transfers.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.EarningUpdate
import java.time.LocalDate

internal data class UpdateEarningDto(
    val date: LocalDate?,
    val received: AccountMoneyDto?,
    val description: String?,
    val categoryId: Long?,
    val version: Int
)

internal fun UpdateEarningDto.toEarningUpdate(id: NumericId<CommonTransfer>) = EarningUpdate(
    id = id,
    date = date,
    received = received?.toAccountMoney(),
    description = description,
    categoryId = categoryId?.asId(),
    version = Version(version)
)
