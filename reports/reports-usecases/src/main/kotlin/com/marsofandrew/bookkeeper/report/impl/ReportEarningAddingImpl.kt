package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.util.toMoney
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportEarningAdding
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionAdder
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.user.User

class ReportEarningAddingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportEarningAdding {

    private val reportEarningAdder = ReportEarningAdder(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun add(earning: Earning) {
        reportEarningAdder.add(earning)
    }

    private class ReportEarningAdder(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionAdder<Earning>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> createReport(
            moneyAction: Earning,
            period: Earning.() -> PeriodType,
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
            val earnings = Report(
                byCategory = mapOf(moneyAction.categoryId to listOf(moneyAction.money)),
                total = listOf(moneyAction.money)
            )

            return creator(
                moneyAction.userId,
                moneyAction.period(),
                Report.empty(),
                earnings,
                Report.empty(),
                earnings.total.map { it.toMoney() },
                Version(0)
            )
        }

        override fun <T, PeriodType> T.add(
            moneyAction: Earning,
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
            require(moneyAction.userId == userId) { "UserId of report ans earning are different" }

            val transferReport = createReport(moneyAction, { period() }, creator)
            val aggregatedResult = aggregate(userId, listOf(this, transferReport), period)

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
