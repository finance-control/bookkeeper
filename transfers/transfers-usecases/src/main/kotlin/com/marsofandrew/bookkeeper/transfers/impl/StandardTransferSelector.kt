package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.category.CategorySelector
import com.marsofandrew.bookkeeper.transfers.impl.utils.validateDates
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

internal class StandardTransferSelector<T : CommonTransferBase>(
    private val transferStorage: TransferStorage,
    private val categorySelector: CategorySelector,
    private val filter: (CommonTransferBase) -> Boolean,
    private val mapper: (CommonTransferBase) -> T
) {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate?,
        endDate: LocalDate,
    ): List<TransferWithCategory<T>> {
        val transfers = if (startDate == null) {
            transferStorage.findAllByUserId(userId)
        } else {
            validateDates(startDate, endDate)

            transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }

        val resultTransfers = transfers
            .filter(filter)
            .map(mapper)

        val categoryById = categorySelector.selectAllByIds(userId, resultTransfers.map { it.categoryId })
            .associateBy { it.id }

        return resultTransfers.map {
            TransferWithCategory(
                transfer = it,
                category = categoryById.getValue(it.categoryId)
            )
        }
    }
}
