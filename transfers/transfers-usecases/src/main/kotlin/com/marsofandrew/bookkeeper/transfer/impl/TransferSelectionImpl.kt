package com.marsofandrew.bookkeeper.transfer.impl

import com.marsofandrew.bookkeeper.transfer.Transfer
import com.marsofandrew.bookkeeper.transfer.TransferSelection
import com.marsofandrew.bookkeeper.transfer.access.TransferStorage
import com.marsofandrew.bookkeeper.transfer.user.User
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