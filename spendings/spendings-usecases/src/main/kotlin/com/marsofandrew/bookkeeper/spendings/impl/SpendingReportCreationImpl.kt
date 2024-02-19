package com.marsofandrew.bookkeeper.spendings.impl

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.SpendingReportCreation
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.SpendingReport
import com.marsofandrew.bookkeeper.spendings.access.SpendingStorage
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

class SpendingReportCreationImpl(
    private val spendingStorage: SpendingStorage
) : SpendingReportCreation {

    override fun createReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate,
        categories: Set<StringId<SpendingCategory>>?
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

    private fun Collection<Spending>.byCategories(): Map<StringId<SpendingCategory>, List<PositiveMoney>> =
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