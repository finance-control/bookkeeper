package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferRemoving
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.addMoney
import com.marsofandrew.bookkeeper.report.impl.util.unaryMinus
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User

class ReportTransferRemovingImpl(
    private val dailyReportStorage: DailyReportStorage,
    private val monthlyReportStorage: MonthlyReportStorage,
    private val yearlyReportStorage: YearlyReportStorage,
    private val transactionalExecution: TransactionalExecution,
) : ReportTransferRemoving {

    override fun remove(transfer: Transfer) {
        transactionalExecution.execute {
            val userId = transfer.userId

            // TODO: throw appropriate exception
            val dailyReport = requireNotNull(dailyReportStorage.findByUserIdAndDate(userId, transfer.date))
            val monthlyReport = requireNotNull(monthlyReportStorage.findByUserIdAndDate(userId, transfer.date))
            val yearlyReport = requireNotNull(yearlyReportStorage.findByUserIdAndDate(userId, transfer.date))

            dailyReportStorage.createOrUpdate(dailyReport.remove(transfer, { date }, ::DailyUserReport))
            monthlyReportStorage.createOrUpdate(monthlyReport.remove(transfer, { month }, ::MonthlyUserReport))
            yearlyReportStorage.createOrUpdate(yearlyReport.remove(transfer, { year }, ::YearlyUserReport))
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
        updateEarnings(earnings, transfer),
        updateTransfers(transfers, transfer),
        total.removeTransfer(transfer)
    )
}

private fun updateTransfers(
    transfersReport: Report<TransferCategory, Money>,
    transfer: Transfer
): Report<TransferCategory, Money> {
    if (transfer.send == null) return transfersReport

    val byCategory = transfersReport.byCategory.toMutableMap()
    byCategory[transfer.transferCategoryId] =
        requireNotNull(byCategory[transfer.transferCategoryId]).removeTransfer(transfer)

    return Report(
        byCategory = byCategory,
        total = transfersReport.total.removeTransfer(transfer)
    )
}

private fun updateEarnings(
    earningsReport: Report<TransferCategory, PositiveMoney>,
    transfer: Transfer
): Report<TransferCategory, PositiveMoney> {
    if (transfer.send != null) return earningsReport

    val byCategory = earningsReport.byCategory.toMutableMap()
    byCategory[transfer.transferCategoryId] =
        requireNotNull(byCategory[transfer.transferCategoryId]).removeTransfer(transfer)

    return Report(
        byCategory = byCategory,
        total = earningsReport.total.removeTransfer(transfer)
    )
}

private fun <T : BaseMoney> List<T>.removeTransfer(transfer: Transfer): List<T> =
    addMoney(transfer.send).addMoney(-transfer.received)
