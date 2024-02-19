package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spendings.SpendingSelection
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

class SpendingSelectionImpl(
    private val spendingStorage: SpendingStorage
) : SpendingSelection {

    override fun select(userId: NumericId<User>, startDate: LocalDate?, endDate: LocalDate): List<Spending> {
        return if (startDate == null) {
            spendingStorage.findAllByUserId(userId)
        } else {
            if (startDate > endDate){
                throw IllegalArgumentException("Start date is more than end date")
            }
            spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }
    }
}