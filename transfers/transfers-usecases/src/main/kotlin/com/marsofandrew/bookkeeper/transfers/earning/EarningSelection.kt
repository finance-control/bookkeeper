package com.marsofandrew.bookkeeper.transfers.earning

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

interface EarningSelection {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate? = null,
        endDate: LocalDate = LocalDate.now().plusDays(1),
    ): List<Earning>
}