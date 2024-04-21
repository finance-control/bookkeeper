package com.marsofandrew.bookkeeper.transfers.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.CommonTransferBase
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

interface TransferStorage {

    fun findAllByUserIdAndIds(userId: NumericId<User>, ids: Collection<NumericId<CommonTransfer>>): Set<CommonTransferBase>
    fun findAllByUserId(userId: NumericId<User>): List<CommonTransferBase>
    fun findAllByUserIdBetween(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<CommonTransferBase>

    fun create(commonTransfer: CommonTransferBase): CommonTransferBase
    fun delete(ids: Collection<NumericId<CommonTransfer>>)
}