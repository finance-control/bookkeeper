package com.marsofandrew.bookkeeper.spending.impl

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.spending.SpendingReportCreation
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingReport
import com.marsofandrew.bookkeeper.spending.access.SpendingStorage
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

class SpendingReportCreationImpl(
    private val spendingStorage: SpendingStorage
) : SpendingReportCreation {

    override fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<NumericId<SpendingCategory>>?
    ): SpendingReport {

        if (startDate > endDate){
            throw IllegalArgumentException("Start date om more than end date")
        }

        val spendings = spendingStorage.findAllByUserIdBetween(userId, startDate, endDate)
            .filter { categories == null || it.spendingCategoryId in categories }

        return SpendingReport(
            spendingByCategory = spendings.byCategories(),
            total = spendings.total()
        )
    }

    private fun Collection<Spending>.byCategories(): Map<NumericId<SpendingCategory>, List<PositiveMoney>> =
        groupBy { it.spendingCategoryId }
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