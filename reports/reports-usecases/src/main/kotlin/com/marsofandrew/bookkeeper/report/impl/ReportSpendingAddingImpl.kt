package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingAdding
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth


class ReportSpendingAddingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportSpendingAdding {

    override fun add(spending: Spending) {
        transactionExecutor.execute {
            val userId = spending.userId
            val date = spending.date

            val dailyReport = dailyUserReportStorage.findByUserIdAndDate(userId, date)
                ?.add(spending, { date }, ::DailyUserReport)
                ?: createReport(spending, { date }, ::DailyUserReport)
            val monthlyReport = monthlyUserReportStorage.findByUserIdAndDate(userId, date)
                ?.add(spending, { month }, ::MonthlyUserReport)
                ?: createReport(spending, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyUserReportStorage.findByUserIdAndDate(userId, date)
                ?.add(spending, { year }, ::YearlyUserReport)
                ?: createReport(spending, { Year.from(date) }, ::YearlyUserReport)

            dailyUserReportStorage.createOrUpdate(dailyReport)
            monthlyUserReportStorage.createOrUpdate(monthlyReport)
            yearlyUserReportStorage.createOrUpdate(yearlyReport)
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
    val aggregatedResult = aggregate(userId, listOf(this, spendingReport), period)

    return creator(
        userId,
        period(),
        aggregatedResult.expenses,
        aggregatedResult.earnings,
        aggregatedResult.transfers,
        aggregatedResult.total
    )
}
