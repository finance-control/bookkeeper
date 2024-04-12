package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User

data class AggregatedUserReportFixture<PeriodType>(
    val userId: NumericId<User>,
    val periods: List<PeriodType>,
) {
    var expenses: Report<Category, PositiveMoney> = Report.empty()
    var earnings: Report<Category, PositiveMoney> = Report.empty()
    var transfers: Report<Category, Money> = Report.empty()
    var total: List<Money> = emptyList()

    fun build() = AggregatedUserReport(
        userId = userId,
        periods = periods,
        expenses = expenses,
        earnings = earnings,
        transfers = transfers,
        total = total
    )
}
