package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferRemoving
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.impl.util.ReportMoneyActionRemover
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User

class ReportTransferRemovingImpl(
    dailyUserReportStorage: DailyUserReportStorage,
    monthlyUserReportStorage: MonthlyUserReportStorage,
    yearlyUserReportStorage: YearlyUserReportStorage,
    transactionExecutor: TransactionExecutor,
) : ReportTransferRemoving {

    private val reportTransferRemover = ReportTransferRemover(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    )

    override fun remove(transfer: Transfer) {
        reportTransferRemover.remove(transfer)
    }

    private class ReportTransferRemover(
        dailyUserReportStorage: DailyUserReportStorage,
        monthlyUserReportStorage: MonthlyUserReportStorage,
        yearlyUserReportStorage: YearlyUserReportStorage,
        transactionExecutor: TransactionExecutor,
    ) : ReportMoneyActionRemover<Transfer>(
        dailyUserReportStorage,
        monthlyUserReportStorage,
        yearlyUserReportStorage,
        transactionExecutor
    ) {
        override fun <T, PeriodType> T.remove(
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
            require(moneyAction.userId == userId) { "UserId of report ans spending are different" }

            return creator(
                userId,
                period(),
                expenses,
                earnings,
                updateTransfers(transfers, moneyAction),
                total.removeTransfer(moneyAction),
                version
            )
        }
    }
}

private fun updateTransfers(
    transfersReport: Report<Category, Money>,
    transfer: Transfer
): Report<Category, Money> {
    val byCategory = transfersReport.byCategory.toMutableMap()
    byCategory[transfer.categoryId] =
        requireNotNull(byCategory[transfer.categoryId]).removeTransfer(transfer)

    return Report(
        byCategory = byCategory,
        total = transfersReport.total.removeTransfer(transfer)
    )
}

private fun List<Money>.removeTransfer(transfer: Transfer): List<Money> =
    addMoney(transfer.send).addMoney(-transfer.received)
