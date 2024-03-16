package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingSelection
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

class SpendingSelectionImpl(
    private val spendingStorage: SpendingStorage
) : SpendingSelection {

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<Spending> {
        return if (startDate == null) {
            spendingStorage.findAllByUserId(userId)
        } else {
            if (startDate > endDate) {
                throw InvalidDateIntervalException(startDate, endDate)
            }
            spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }
    }
}
