package com.marsofandrew.bookkeeper.transfers.transfer

import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.TransferReport
import java.time.LocalDate

interface TransferReportCreation {

    fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): TransferReport
}