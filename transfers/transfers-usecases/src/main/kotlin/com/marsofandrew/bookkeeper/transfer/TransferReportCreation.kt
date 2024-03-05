package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.transfer.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.LocalDate

interface TransferReportCreation {

    fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): TransferReport
}