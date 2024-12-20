package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year

data class YearlyUserReportFixture(
    val userId: NumericId<User>,
    val year: Year,
) {
    var expenses: Report<Category, PositiveMoney> = Report.empty()
    var earnings: Report<Category, PositiveMoney> = Report.empty()
    var transfers: Report<Category, Money> = Report.empty()
    var total: List<Money> = emptyList()
    var version = Version(0)

    fun build() = YearlyUserReport(
        userId = userId,
        year = year,
        expenses = expenses,
        earnings = earnings,
        transfers = transfers,
        total = total,
        version = version
    )
}
