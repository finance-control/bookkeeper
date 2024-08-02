package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.impl.utils.validateDates
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

internal class StandardTransferSelector<T : CommonTransferBase>(
    private val transferStorage: TransferStorage,
    private val filter: (CommonTransferBase) -> Boolean,
    private val mapper: (CommonTransferBase) -> T
) {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate?,
        endDate: LocalDate,
    ): List<T> {
        val transfers = if (startDate == null) {
            transferStorage.findAllByUserId(userId)
        } else {
            validateDates(startDate, endDate)

            transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }

        return transfers
            .filter(filter)
            .map(mapper)
    }
}