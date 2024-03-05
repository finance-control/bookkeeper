package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.transfer.user.User
import com.marsofandrew.bookkeeper.properties.id.NumericId
import java.time.LocalDate

interface TransferSelection {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate? = null,
        endDate: LocalDate = LocalDate.now().plusDays(1),
    ): List<Transfer>
}