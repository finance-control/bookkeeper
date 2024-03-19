package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingRemoving
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionRemover
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.addNegative
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User

class ReportSpendingRemovingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportSpendingRemoving {

    private val reportSpendingRemover = ReportSpendingRemover(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun remove(spending: Spending) {
        reportSpendingRemover.remove(spending)
    }

    private class ReportSpendingRemover(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionRemover<Spending>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> T.remove(
            moneyAction: Spending,
            period: T.() -> PeriodType,
            creator: (
                userId: NumericId<User>,
                period: PeriodType,
                expenses: Report<SpendingCategory, PositiveMoney>,
                earnings: Report<TransferCategory, PositiveMoney>,
                transfers: Report<TransferCategory, Money>,
                total: List<Money>,
                version: Version
            ) -> T
        ): T where T : BaseUserReport, T : DomainModel {
            require(moneyAction.userId == userId) { "UserId of report ans spending are different" }

            val updatedExpensesByCategory = expenses.byCategory.toMutableMap()
            updatedExpensesByCategory[moneyAction.categoryId] =
                requireNotNull(updatedExpensesByCategory[moneyAction.categoryId])
                    .addNegative(-moneyAction.money)

            val updatedTotal = total.addMoney(moneyAction.money)

            return creator(
                userId,
                period(),
                Report(
                    byCategory = updatedExpensesByCategory
                        .filterValues { it.isNotEmpty() },
                    total = expenses.total.addNegative(-moneyAction.money)
                ),
                earnings,
                transfers,
                updatedTotal,
                version
            )
        }
    }
}
