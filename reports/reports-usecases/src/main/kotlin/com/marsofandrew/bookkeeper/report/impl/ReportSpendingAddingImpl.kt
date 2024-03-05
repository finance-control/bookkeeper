package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingAdding
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth


class ReportSpendingAddingImpl(
    private val dailyReportStorage: DailyReportStorage,
    private val monthlyReportStorage: MonthlyReportStorage,
    private val yearlyReportStorage: YearlyReportStorage,
    private val transactionalExecution: TransactionalExecution,
) : ReportSpendingAdding {

    override fun add(spending: Spending) {
        transactionalExecution.execute {
            val userId = spending.userId
            val date = spending.date

            val dailyReport = dailyReportStorage.findByUserIdAndDate(userId, date)
                ?: createReport(spending, { date }, ::DailyUserReport)
            val monthlyReport = monthlyReportStorage.findByUserIdAndDate(userId, date)
                ?: createReport(spending, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyReportStorage.findByUserIdAndDate(userId, date)
                ?: createReport(spending, { Year.from(date) }, ::YearlyUserReport)

            dailyReportStorage.createOrUpdate(dailyReport.add(spending, { date }, ::DailyUserReport))
            monthlyReportStorage.createOrUpdate(monthlyReport.add(spending, { month }, ::MonthlyUserReport))
            yearlyReportStorage.createOrUpdate(yearlyReport.add(spending, { year }, ::YearlyUserReport))
        }
    }
}

private fun <T : BaseUserReport, PeriodType> createReport(
    spending: Spending,
    period: Spending.() -> PeriodType,
    creator: (
        userId: NumericId<User>,
        period: PeriodType,
        expenses: Report<SpendingCategory, PositiveMoney>,
        earnings: Report<TransferCategory, PositiveMoney>,
        transfers: Report<TransferCategory, Money>,
        total: List<Money>
    ) -> T
): T {
    return creator(
        spending.userId,
        spending.period(),
        Report(
            byCategory = mapOf(spending.spendingCategoryId to listOf(spending.money)),
            total = listOf(spending.money)
        ),
        Report.empty(),
        Report.empty(),
        listOf(Money(spending.money.currency, -spending.money.amount))
    )
}
private fun <T : BaseUserReport, PeriodType> T.add(
    spending: Spending,
    period: T.() -> PeriodType,
    creator: (
        userId: NumericId<User>,
        period: PeriodType,
        expenses: Report<SpendingCategory, PositiveMoney>,
        earnings: Report<TransferCategory, PositiveMoney>,
        transfers: Report<TransferCategory, Money>,
        total: List<Money>
    ) -> T
): T {
    require(spending.userId == userId) { "UserId of report ans spending are different" }

    val spendingReport = createReport(spending, { period() }, creator)
    val aggregatedResult = aggregate(listOf(this, spendingReport), period)

    return creator(
        userId,
        period(),
        aggregatedResult.expenses,
        aggregatedResult.earnings,
        aggregatedResult.transfers,
        aggregatedResult.total
    )
}
