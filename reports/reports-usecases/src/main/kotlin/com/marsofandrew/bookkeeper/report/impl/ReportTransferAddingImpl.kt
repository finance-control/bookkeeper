package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.ReportTransferAdding
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.impl.util.totalMoney
import com.marsofandrew.bookkeeper.report.transfer.Transfer
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth

class ReportTransferAddingImpl(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) : ReportTransferAdding {

    override fun add(transfer: Transfer) {
        transactionExecutor.execute {
            val userId = transfer.userId

            val dailyReport = dailyUserReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?.add(transfer, { date }, ::DailyUserReport)
                ?: createReport(transfer, { date }, ::DailyUserReport)
            val monthlyReport = monthlyUserReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?.add(transfer, { month }, ::MonthlyUserReport)
                ?: createReport(transfer, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyUserReportStorage.findByUserIdAndDate(userId, transfer.date)
                ?.add(transfer, { year }, ::YearlyUserReport)
                ?: createReport(transfer, { Year.from(date) }, ::YearlyUserReport)

            dailyUserReportStorage.createOrUpdate(dailyReport)
            monthlyUserReportStorage.createOrUpdate(monthlyReport)
            yearlyUserReportStorage.createOrUpdate(yearlyReport)
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

    val transfers = Report(
        byCategory = mapOf(transfer.transferCategoryId to transfer.totalMoney),
        total = transfer.totalMoney
    )

    return creator(transfer.userId, transfer.period(), Report.empty(), Report.empty(), transfers, transfer.totalMoney)
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
    require(transfer.userId == userId) { "UserId of report ans transfer are different" }

    val transferReport = createReport(transfer, { period() }, creator)
    val aggregatedResult = aggregate(userId, listOf(this, transferReport), period)

    return creator(
        userId,
        period(),
        aggregatedResult.expenses,
        aggregatedResult.earnings,
        aggregatedResult.transfers,
        aggregatedResult.total
    )
}
