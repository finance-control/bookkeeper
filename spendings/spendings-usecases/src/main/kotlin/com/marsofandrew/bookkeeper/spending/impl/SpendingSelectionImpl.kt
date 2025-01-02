package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingSelection
import com.marsofandrew.bookkeeper.spending.SpendingWithCategory
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.CategorySelector
import com.marsofandrew.bookkeeper.spending.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

class SpendingSelectionImpl(
    private val spendingStorage: SpendingStorage,
    private val categorySelector: CategorySelector
) : SpendingSelection {

    override fun select(
        userId: NumericId<User>,
        startDate: LocalDate?,
        endDate: LocalDate
    ): List<SpendingWithCategory> {
        val spendings = if (startDate == null) {
            spendingStorage.findAllByUserId(userId)
        } else {
            if (startDate > endDate) {
                throw InvalidDateIntervalException(startDate, endDate)
            }
            spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
        }

        val categoryById = categorySelector.selectAllByIds(userId, spendings.map { it.categoryId }.distinct())
            .associateBy { it.id }

        return spendings.map {
            SpendingWithCategory(
                spending = it,
                category = categoryById.getValue(it.categoryId)
            )
        }
    }
}
