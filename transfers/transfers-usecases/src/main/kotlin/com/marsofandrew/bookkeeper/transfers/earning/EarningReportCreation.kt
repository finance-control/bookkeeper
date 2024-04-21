package com.marsofandrew.bookkeeper.transfers.earning

import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.TransferReport
import java.time.LocalDate

interface EarningReportCreation {

    fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): TransferReport
}