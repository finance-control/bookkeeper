package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingReport
import com.marsofandrew.bookkeeper.spending.SpendingReportCreation
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

class SpendingReportCreationImpl(
    private val spendingStorage: SpendingStorage
) : SpendingReportCreation {

    override fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<NumericId<Category>>?
    ): SpendingReport {

        if (startDate > endDate) {
            throw InvalidDateIntervalException(startDate, endDate)
        }

        val spendings = spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .filter { categories == null || it.categoryId in categories }

        return SpendingReport(
            spendingByCategory = spendings.byCategories(),
            total = spendings.total()
        )
    }

    private fun Collection<Spending>.byCategories(): Map<NumericId<Category>, List<PositiveMoney>> =
        groupBy { it.categoryId }
            .mapValues { (_, values) -> values.total() }


    private fun Collection<Spending>.total(): List<PositiveMoney> = map { it.money }
        .groupBy { it.currency }
        .mapValues { (_, money) ->
            money.map { it.amount }
                .reduce { acc, value -> acc.add(value) }
        }
        .entries
        .map { (currency, amount) -> PositiveMoney(currency, amount) }
}
