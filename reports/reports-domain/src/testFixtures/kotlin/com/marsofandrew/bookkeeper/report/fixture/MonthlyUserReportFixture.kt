package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

data class MonthlyUserReportFixture(
    val userId: NumericId<User>,
    val month: YearMonth,
) {
    var expenses: Report<Category, PositiveMoney> = Report.empty()
    var earnings: Report<Category, PositiveMoney> = Report.empty()
    var transfers: Report<Category, Money> = Report.empty()
    var total: List<Money> = emptyList()
    var version = Version(0)

    fun build() = MonthlyUserReport(
        userId = userId,
        month = month,
        expenses = expenses,
        earnings = earnings,
        transfers = transfers,
        total = total,
        version = version
    )
}
