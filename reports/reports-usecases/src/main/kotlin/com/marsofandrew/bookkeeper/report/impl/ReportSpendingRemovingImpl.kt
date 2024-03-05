package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingRemoving
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User

class ReportSpendingRemovingImpl(
    private val dailyReportStorage: DailyReportStorage,
    private val monthlyReportStorage: MonthlyReportStorage,
    private val yearlyReportStorage: YearlyReportStorage,
    private val transactionalExecution: TransactionalExecution,
) : ReportSpendingRemoving {

    override fun remove(spending: Spending) {
        transactionalExecution.execute {
            val userId = spending.userId

            // TODO: throw appropriate exception
            val dailyReport = requireNotNull(dailyReportStorage.findByUserIdAndDate(userId, spending.date))
            val monthlyReport = requireNotNull(monthlyReportStorage.findByUserIdAndDate(userId, spending.date))
            val yearlyReport = requireNotNull(yearlyReportStorage.findByUserIdAndDate(userId, spending.date))

            dailyReportStorage.createOrUpdate(dailyReport.remove(spending, { date }, ::DailyUserReport))
            monthlyReportStorage.createOrUpdate(monthlyReport.remove(spending, { month }, ::MonthlyUserReport))
            yearlyReportStorage.createOrUpdate(yearlyReport.remove(spending, { year }, ::YearlyUserReport))
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
        requireNotNull(updatedExpensesByCategory[spending.spendingCategoryId]).addMoney(-spending.money)

    val updatedTotal = total.addMoney(spending.money)

    return creator(
        userId,
        period(),
        Report(
            byCategory = updatedExpensesByCategory,
            total = expenses.total.addMoney(-spending.money)
        ),
        earnings,
        transfers,
        updatedTotal
    )
}
