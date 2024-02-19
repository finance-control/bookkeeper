package com.marsofandrew.bookkeeper.transfers.impl

import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.TransferSelection
import com.marsofandrew.bookkeeper.transfers.access.TransferStorage
import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.LocalDate

class TransferSelectionImpl(
    private val transferStorage: TransferStorage
) : TransferSelection {

    override fun select(
        userId: NumericId<User>,
        startDate: LocalDate?,
        endDate: LocalDate,
    ): List<Transfer> {
        return if (startDate == null) {
            transferStorage.findAllByUserId(userId)
        } else {
            if (startDate > endDate){
                throw IllegalArgumentException("Start date om more than end date")
            }
            transferStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }
    }


}