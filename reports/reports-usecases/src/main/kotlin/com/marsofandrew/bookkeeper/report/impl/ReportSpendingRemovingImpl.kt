package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingRemoving
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.addNegative
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User

class ReportSpendingRemovingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportSpendingRemoving {

    override fun remove(spending: Spending) {
        transactionExecutor.execute {
            val userId = spending.userId

            // TODO: throw appropriate exception
            val dailyReport = requireNotNull(dailyUserReportStorage.findByUserIdAndDate(userId, spending.date))
            val monthlyReport = requireNotNull(monthlyUserReportStorage.findByUserIdAndDate(userId, spending.date))
            val yearlyReport = requireNotNull(yearlyUserReportStorage.findByUserIdAndDate(userId, spending.date))

            dailyUserReportStorage.createOrUpdate(dailyReport.remove(spending, { date }, ::DailyUserReport))
            monthlyUserReportStorage.createOrUpdate(monthlyReport.remove(spending, { month }, ::MonthlyUserReport))
            yearlyUserReportStorage.createOrUpdate(yearlyReport.remove(spending, { year }, ::YearlyUserReport))
        }
    }
}

private fun <T : BaseUserReport, PeriodType> T.remove(
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

    val updatedExpensesByCategory = expenses.byCategory.toMutableMap()
    updatedExpensesByCategory[spending.spendingCategoryId] =
        requireNotNull(updatedExpensesByCategory[spending.spendingCategoryId])
            .addNegative(-spending.money)


    val updatedTotal = total.addMoney(spending.money)

    return creator(
        userId,
        period(),
        Report(
            byCategory = updatedExpensesByCategory
                .filterValues { it.isNotEmpty() },
            total = expenses.total.addNegative(-spending.money)
        ),
        earnings,
        transfers,
        updatedTotal
    )
}
