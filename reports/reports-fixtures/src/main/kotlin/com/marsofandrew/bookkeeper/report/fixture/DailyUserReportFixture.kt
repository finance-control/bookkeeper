package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class DailyUserReportFixture(
    val userId: NumericId<User>,
    val date: LocalDate,
) {
    var expenses: Report<SpendingCategory, PositiveMoney> = Report.empty()
    var earnings: Report<TransferCategory, PositiveMoney> = Report.empty()
    var transfers: Report<TransferCategory, Money> = Report.empty()
    var total: List<Money> = emptyList()

    fun build() = DailyUserReport(
        userId = userId,
        date = date,
        expenses = expenses,
        earnings = earnings,
        transfers = transfers,
        total = total
    )
}
