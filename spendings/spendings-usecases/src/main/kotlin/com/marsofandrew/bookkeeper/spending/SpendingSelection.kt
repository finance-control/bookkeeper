package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

interface SpendingSelection {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate? = null,
        endDate: LocalDate = LocalDate.now().plusDays(1),
    ): List<Spending>
}