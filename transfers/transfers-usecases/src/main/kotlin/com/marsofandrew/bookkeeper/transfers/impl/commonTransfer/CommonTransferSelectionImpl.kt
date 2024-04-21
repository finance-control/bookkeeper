package com.marsofandrew.bookkeeper.transfers.impl.commonTransfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.CommonTransferSelection
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferSelector
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class CommonTransferSelectionImpl(
    transferStorage: TransferStorage
) : CommonTransferSelection {

    private val standardTransferSelector = StandardTransferSelector(transferStorage, { true }) { CommonTransfer.of(it) }

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<CommonTransfer> {
        return standardTransferSelector.select(userId, startDate, endDate)
    }
}
