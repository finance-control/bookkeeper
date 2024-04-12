package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportSpendingAdding
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionAdder
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.spending.Spending
import com.marsofandrew.bookkeeper.report.user.User


class ReportSpendingAddingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportSpendingAdding {

    private val reportSpendingAdder = ReportSpendingAdder(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun add(spending: Spending) {
        reportSpendingAdder.add(spending)
    }

    private class ReportSpendingAdder(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionAdder<Spending>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> createReport(
            moneyAction: Spending,
            period: Spending.() -> PeriodType,
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
            return creator(
                moneyAction.userId,
                moneyAction.period(),
                Report(
                    byCategory = mapOf(moneyAction.categoryId to listOf(moneyAction.money)),
                    total = listOf(moneyAction.money)
                ),
                Report.empty(),
                Report.empty(),
                listOf(Money(moneyAction.money.currency, -moneyAction.money.amount)),
                Version(0)
            )
        }

        override fun <T, PeriodType> T.add(
            moneyAction: Spending,
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
            require(moneyAction.userId == userId) { "UserId of report ans spending are different" }

            val spendingReport = createReport(moneyAction, { period() }, creator)
            val aggregatedResult = aggregate(userId, listOf(this, spendingReport), period)

            return creator(
                userId,
                period(),
                aggregatedResult.expenses,
                aggregatedResult.earnings,
                aggregatedResult.transfers,
                aggregatedResult.total,
                version
            )
        }
    }
}
