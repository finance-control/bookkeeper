package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingReport
import com.marsofandrew.bookkeeper.spending.SpendingReportCreation
import com.marsofandrew.bookkeeper.spending.SpendingReportsWithCategories
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.Category
import com.marsofandrew.bookkeeper.spending.category.CategorySelector
import com.marsofandrew.bookkeeper.spending.exception.InvalidDateIntervalException
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

class SpendingReportCreationImpl(
    private val spendingStorage: SpendingStorage,
    private val categorySelector: CategorySelector,
) : SpendingReportCreation {

    override fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<NumericId<Category>>?
    ): SpendingReportsWithCategories {

        if (startDate > endDate) {
            throw InvalidDateIntervalException(startDate, endDate)
        }

        val spendings = spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .filter { categories == null || it.categoryId in categories }

        val report =  SpendingReport(
            spendingByCategory = spendings.byCategories(),
            total = spendings.total(),
        )

        val reportCategories = spendings.map { it.categoryId }
            .distinct()
            .toList()

        val categoriesById = categorySelector.selectAllByIds(userId, reportCategories)
            .associateBy { it.id }

        return SpendingReportsWithCategories(
            report = report,
            categories = categoriesById
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
