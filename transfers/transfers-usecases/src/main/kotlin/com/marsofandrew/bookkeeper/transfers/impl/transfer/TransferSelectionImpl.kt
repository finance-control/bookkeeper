package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferWithCategory
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.category.TransferCategorySelector
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferSelector
import com.marsofandrew.bookkeeper.transfers.transfer.TransferSelection
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class TransferSelectionImpl(
    transferStorage: TransferStorage,
    transferCategorySelector: TransferCategorySelector,
) : TransferSelection {

    private val standardTransferSelector =
        StandardTransferSelector(transferStorage, transferCategorySelector,{ it.send != null }) { Transfer.of(it) }

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<TransferWithCategory<Transfer>> {
        return standardTransferSelector.select(userId, startDate, endDate)
    }
}