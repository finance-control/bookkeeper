package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportEarningRemoving
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.addNegative
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.user.User

class ReportEarningRemovingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportEarningRemoving {

    override fun remove(earning: Earning) {
        transactionExecutor.execute {
            val userId = earning.userId

            // TODO: throw appropriate exception
            val dailyReport = requireNotNull(dailyUserReportStorage.findByUserIdAndDate(userId, earning.date))
            val monthlyReport = requireNotNull(monthlyUserReportStorage.findByUserIdAndDate(userId, earning.date))
            val yearlyReport = requireNotNull(yearlyUserReportStorage.findByUserIdAndDate(userId, earning.date))

            dailyUserReportStorage.createOrUpdate(dailyReport.remove(earning, { date }, ::DailyUserReport))
            monthlyUserReportStorage.createOrUpdate(monthlyReport.remove(earning, { month }, ::MonthlyUserReport))
            yearlyUserReportStorage.createOrUpdate(yearlyReport.remove(earning, { year }, ::YearlyUserReport))
        }
    }
}

private fun <T : BaseUserReport, PeriodType> T.remove(
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
    require(earning.userId == userId) { "UserId of report and earning are different" }

    val updatedEarningsByCategory = earnings.byCategory.toMutableMap()
    updatedEarningsByCategory[earning.transferCategoryId] =
        requireNotNull(updatedEarningsByCategory[earning.transferCategoryId])
            .addNegative(-earning.money)


    val updatedTotal = total.addMoney(-earning.money)

    return creator(
        userId,
        period(),
        expenses,
        Report(
            byCategory = updatedEarningsByCategory
                .filterValues { it.isNotEmpty() },
            total = earnings.total.addNegative(-earning.money)
        ),
        transfers,
        updatedTotal
    )
}