package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportEarningAdding
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.impl.util.toMoney
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth

class ReportEarningAddingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportEarningAdding {

    override fun add(earning: Earning) {
        transactionExecutor.execute {
            val userId = earning.userId

            val dailyReport = dailyUserReportStorage.findByUserIdAndDate(userId, earning.date)
                ?.add(earning, { date }, ::DailyUserReport)
                ?: createReport(earning, { date }, ::DailyUserReport)
            val monthlyReport = monthlyUserReportStorage.findByUserIdAndDate(userId, earning.date)
                ?.add(earning, { month }, ::MonthlyUserReport)
                ?: createReport(earning, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyUserReportStorage.findByUserIdAndDate(userId, earning.date)
                ?.add(earning, { year }, ::YearlyUserReport)
                ?: createReport(earning, { Year.from(date) }, ::YearlyUserReport)


            dailyUserReportStorage.createOrUpdate(dailyReport)
            monthlyUserReportStorage.createOrUpdate(monthlyReport)
            yearlyUserReportStorage.createOrUpdate(yearlyReport)
        }
    }
}

private fun <T : BaseUserReport, PeriodType> createReport(
    earning: Earning,
    period: Earning.() -> PeriodType,
    creator: (
        userId: NumericId<User>,
        period: PeriodType,
        expenses: Report<SpendingCategory, PositiveMoney>,
        earnings: Report<TransferCategory, PositiveMoney>,
        transfers: Report<TransferCategory, Money>,
        total: List<Money>
    ) -> T
): T {
    val earnings = Report(
        byCategory = mapOf(earning.transferCategoryId to listOf(earning.money)),
        total = listOf(earning.money)
    )

    return creator(
        earning.userId,
        earning.period(),
        Report.empty(),
        earnings,
        Report.empty(),
        earnings.total.map { it.toMoney() })
}

private fun <T : BaseUserReport, PeriodType> T.add(
    earning: Earning,
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
    require(earning.userId == userId) { "UserId of report ans earning are different" }

    val transferReport = createReport(earning, { period() }, creator)
    val aggregatedResult = aggregate(userId, listOf(this, transferReport), period)

    return creator(
        userId,
        period(),
        aggregatedResult.expenses,
        aggregatedResult.earnings,
        aggregatedResult.transfers,
        aggregatedResult.total
    )
}