package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class DailyUserReportFixture(
    val userId: NumericId<User>,
    val date: LocalDate,
) {
    var expenses: Report<Category, PositiveMoney> = Report.empty()
    var earnings: Report<Category, PositiveMoney> = Report.empty()
    var transfers: Report<Category, Money> = Report.empty()
    var total: List<Money> = emptyList()
    var version = Version(0)

    fun build() = DailyUserReport(
        userId = userId,
        date = date,
        expenses = expenses,
        earnings = earnings,
        transfers = transfers,
        total = total,
        version = version
    )
}
