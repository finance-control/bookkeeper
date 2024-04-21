package com.marsofandrew.bookkeeper.transfers.impl.commonTransfer

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferReportCreation
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransfersReportCreator
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class CommonTransfersReportCreationImpl(
    transferStorage: TransferStorage
) : CommonTransferReportCreation {

    private val standardTransfersReportCreator = StandardTransfersReportCreator(transferStorage) { true }

    override fun createReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): TransferReport {
        return standardTransfersReportCreator.createReport(userId, startDate, endDate)
    }
}
