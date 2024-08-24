package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import java.time.LocalDate

data class TransferUpdate(
    override val id: NumericId<CommonTransfer>,
    override val date: LocalDate?,
    override val received: AccountMoney?,
    override val description: String?,
    override val categoryId: NumericId<Category>?,
    override val send: AccountMoney?,
    override val version: Version
) : CommonTransferUpdateBase {

    init {
        validateFiled(date == null || date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}

fun CommonTransferBase.updateTransfer(update: TransferUpdate): Transfer = Transfer(
    id = id,
    date = update.date ?: date,
    send = update.send ?: send!!,
    received = update.received ?: received,
    description = update.description ?: description,
    categoryId = update.categoryId ?: categoryId,
    version = update.version,
    createdAt = createdAt,
    userId = userId
)
