package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferSelector
import com.marsofandrew.bookkeeper.transfers.transfer.TransferSelection
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class TransferSelectionImpl(
    transferStorage: TransferStorage
) : TransferSelection {

    private val standardTransferSelector =
        StandardTransferSelector(transferStorage, { it.send != null }) { Transfer.of(it) }

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<Transfer> {
        return standardTransferSelector.select(userId, startDate, endDate)
    }
}