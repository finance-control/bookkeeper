package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferAdding
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionAdder
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.impl.util.totalMoney
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User

class ReportTransferAddingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportTransferAdding {

    private val reportTransferAdder = ReportTransferAdder(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun add(transfer: Transfer) {
        reportTransferAdder.add(transfer)
    }

    private class ReportTransferAdder(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionAdder<Transfer>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> createReport(
            moneyAction: Transfer,
            period: Transfer.() -> PeriodType,
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
            val transfers = Report(
                byCategory = mapOf(moneyAction.categoryId to moneyAction.totalMoney),
                total = moneyAction.totalMoney
            )

            return creator(
                moneyAction.userId,
                moneyAction.period(),
                Report.empty(),
                Report.empty(),
                transfers,
                moneyAction.totalMoney,
                Version(0)
            )
        }

        override fun <T, PeriodType> T.add(
            moneyAction: Transfer,
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
            require(moneyAction.userId == userId) { "UserId of report ans transfer are different" }

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
