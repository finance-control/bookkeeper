package com.marsofandrew.bookkeeper.transfers.impl.earning

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.TransferReport
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.earning.EarningReportCreation
import com.marsofandrew.bookkeeper.transfers.impl.StandardTransfersReportCreator
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

class EarningReportCreationImpl(
    transferStorage: TransferStorage
) : EarningReportCreation {

    private val standardTransfersReportCreator = StandardTransfersReportCreator(transferStorage) { it.send == null }

    override fun createReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): TransferReport {
        return standardTransfersReportCreator.createReport(userId, startDate, endDate)
    }
}