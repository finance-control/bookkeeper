package com.marsofandrew.bookkeeper.transfers.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

interface TransferStorage {

    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<Transfer>>): Set<Transfer>
    fun findAllByUserId(userId: NumericId<User>): List<Transfer>
    fun findAllByUserIdBetween(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<Transfer>

    fun create(transfer: Transfer): Transfer
    fun delete(ids: Collection<NumericId<Transfer>>)
}