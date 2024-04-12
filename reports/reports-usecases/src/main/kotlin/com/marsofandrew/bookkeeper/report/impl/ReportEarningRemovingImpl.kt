package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportEarningRemoving
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionRemover
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.addNegative
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.user.User

class ReportEarningRemovingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportEarningRemoving {

    private val reportEarningRemover = ReportEarningRemover(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun remove(earning: Earning) {
        reportEarningRemover.remove(earning)
    }

    private class ReportEarningRemover(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionRemover<Earning>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> T.remove(
            moneyAction: Earning,
            period: T.() -> PeriodType,
            creator: (
                userId: NumericId<User>,
                period: PeriodType,
                expenses: Report<Category, PositiveMoney>,
                earnings: Report<Category, PositiveMoney>,
                transfers: Report<Category, Money>,
                total: List<Money>,
                version: Version
            ) -> T
        ): T where T : BaseUserReport, T : DomainModel {
            require(moneyAction.userId == userId) { "UserId of report and earning are different" }

            val updatedEarningsByCategory = earnings.byCategory.toMutableMap()
            updatedEarningsByCategory[moneyAction.categoryId] =
                requireNotNull(updatedEarningsByCategory[moneyAction.categoryId])
                    .addNegative(-moneyAction.money)

            val updatedTotal = total.addMoney(-moneyAction.money)

            return creator(
                userId,
                period(),
                expenses,
                Report(
                    byCategory = updatedEarningsByCategory
                        .filterValues { it.isNotEmpty() },
                    total = earnings.total.addNegative(-moneyAction.money)
                ),
                transfers,
                updatedTotal,
                version
            )
        }
    }
}
