package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionalExecution
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferAdding
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.impl.util.totalMoney
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth

class ReportTransferAddingImpl(
    private val dailyReportStorage: DailyReportStorage,
    private val monthlyReportStorage: MonthlyReportStorage,
    private val yearlyReportStorage: YearlyReportStorage,
    private val transactionalExecution: TransactionalExecution,
) : ReportTransferAdding {

    override fun add(transfer: Transfer) {
        transactionalExecution.execute {
            val userId = transfer.userId

            val dailyReport = dailyReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?: createReport(transfer, { date }, ::DailyUserReport)
            val monthlyReport = monthlyReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?: createReport(transfer, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?: createReport(transfer, { Year.from(date) }, ::YearlyUserReport)

            dailyReportStorage.createOrUpdate(dailyReport.add(transfer, { date }, ::DailyUserReport))
            monthlyReportStorage.createOrUpdate(monthlyReport.add(transfer, { month }, ::MonthlyUserReport))
            yearlyReportStorage.createOrUpdate(yearlyReport.add(transfer, { year }, ::YearlyUserReport))
        }
    }
}

private fun <T : BaseUserReport, PeriodType> createReport(
    transfer: Transfer,
    period: Transfer.() -> PeriodType,
    creator: (
        userId: NumericId<User>,
        period: PeriodType,
        expenses: Report<SpendingCategory, PositiveMoney>,
        earnings: Report<TransferCategory, PositiveMoney>,
        transfers: Report<TransferCategory, Money>,
        total: List<Money>
    ) -> T
): T {
    val earnings = if (transfer.send == null) {
        val totalEarnings = listOf(transfer.received)
        Report(
            byCategory = mapOf(transfer.transferCategoryId to totalEarnings),
            total = totalEarnings
        )
    } else Report.empty()

    val transfers = if (transfer.send != null) {
        Report(
            byCategory = mapOf(transfer.transferCategoryId to transfer.totalMoney),
            total = transfer.totalMoney
        )
    } else Report.empty()

    return creator(transfer.userId, transfer.period(), Report.empty(), earnings, transfers, transfer.totalMoney)
}

private fun <T : BaseUserReport, PeriodType> T.add(
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

    val transferReport = createReport(transfer, { period() }, creator)
    val aggregatedResult = aggregate(listOf(this, transferReport), period)

    return creator(
        userId,
        period(),
        aggregatedResult.expenses,
        aggregatedResult.earnings,
        aggregatedResult.transfers,
        aggregatedResult.total
    )
}
