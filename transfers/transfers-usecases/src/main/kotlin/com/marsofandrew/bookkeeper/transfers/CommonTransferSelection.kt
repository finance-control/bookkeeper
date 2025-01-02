package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.transfers.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.LocalDate

interface CommonTransferSelection {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate? = null,
        endDate: LocalDate = LocalDate.now().plusDays(1),
    ): List<TransferWithCategory<CommonTransfer>>
}
