package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.category.Category
import java.time.LocalDate

data class EarningUpdate(
    override val id: NumericId<CommonTransfer>,
    override val date: LocalDate?,
    override val received: AccountMoney?,
    override val description: String?,
    override val categoryId: NumericId<Category>?,
    override val version: Version
) : CommonTransferUpdateBase {
    override val send: AccountMoney? = null

    init {
        validateFiled(date == null || date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}

fun CommonTransferBase.updateEarning(update: EarningUpdate): Earning = Earning(
    id = id,
    date = update.date ?: date,
    received = update.received ?: received,
    description = update.description ?: description,
    categoryId = update.categoryId ?: categoryId,
    version = update.version,
    createdAt = createdAt,
    userId = userId
)
