package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferRemoving
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User

class ReportTransferRemovingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportTransferRemoving {

    override fun remove(transfer: Transfer) {
        transactionExecutor.execute {
            val userId = transfer.userId

            // TODO: throw appropriate exception
            val dailyReport = requireNotNull(dailyUserReportStorage.findByUserIdAndDate(userId, transfer.date))
            val monthlyReport = requireNotNull(monthlyUserReportStorage.findByUserIdAndDate(userId, transfer.date))
            val yearlyReport = requireNotNull(yearlyUserReportStorage.findByUserIdAndDate(userId, transfer.date))

            dailyUserReportStorage.createOrUpdate(dailyReport.remove(transfer, { date }, ::DailyUserReport))
            monthlyUserReportStorage.createOrUpdate(monthlyReport.remove(transfer, { month }, ::MonthlyUserReport))
            yearlyUserReportStorage.createOrUpdate(yearlyReport.remove(transfer, { year }, ::YearlyUserReport))
        }
    }
}

private fun <T : BaseUserReport, PeriodType> T.remove(
    transfer: Transfer,
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
    require(transfer.userId == userId) { "UserId of report ans spending are different" }

    return creator(
        userId,
        period(),
        expenses,
        earnings,
        updateTransfers(transfers, transfer),
        total.removeTransfer(transfer)
    )
}

private fun updateTransfers(
    transfersReport: Report<TransferCategory, Money>,
    transfer: Transfer
): Report<TransferCategory, Money> {
    val byCategory = transfersReport.byCategory.toMutableMap()
    byCategory[transfer.transferCategoryId] =
        requireNotNull(byCategory[transfer.transferCategoryId]).removeTransfer(transfer)

    return Report(
        byCategory = byCategory,
        total = transfersReport.total.removeTransfer(transfer)
    )
}

private fun List<Money>.removeTransfer(transfer: Transfer): List<Money> =
    addMoney(transfer.send).addMoney(-transfer.received)
