package com.marsofandrew.bookkeeper.transfers.impl.transfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransferCreator
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransfersReportCreator
import com.marsofandrew.bookkeeper.transfers.transfer.TransferAdding
import com.marsofandrew.bookkeeper.transfers.transfer.TransferReportCreation
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class TransferReportCreationImpl(
    transferStorage: TransferStorage
) : TransferReportCreation {

    private val standardTransfersReportCreator = StandardTransfersReportCreator(transferStorage) { it.send != null }

    override fun createReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): TransferReport {
        return standardTransfersReportCreator.createReport(userId, startDate, endDate)
    }
}
