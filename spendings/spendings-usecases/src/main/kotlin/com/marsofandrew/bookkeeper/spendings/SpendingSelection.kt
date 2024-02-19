package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

interface SpendingSelection {

    fun select(
        userId: NumericId<User>,
        startDate: LocalDate? = null,
        endDate: LocalDate = LocalDate.now().plusDays(1),
    ): List<Spending>
}