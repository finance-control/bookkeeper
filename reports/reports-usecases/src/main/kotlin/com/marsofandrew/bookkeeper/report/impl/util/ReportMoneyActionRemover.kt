package com.marsofandrew.bookkeeper.report.impl.util

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.base.transaction.TransactionExecutor
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.Report
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.access.YearlyUserReportStorage
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.common.WithDate
import com.marsofandrew.bookkeeper.report.common.WithUserId
import com.marsofandrew.bookkeeper.report.user.User

internal abstract class ReportMoneyActionRemover<MoneyAction>(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) where MoneyAction : WithUserId, MoneyAction : WithDate {

    fun remove(moneyAction: MoneyAction) {
        transactionExecutor.execute {
            // TODO: throw appropriate exception
            val dailyReport =
                requireNotNull(dailyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date))
            val monthlyReport =
                requireNotNull(monthlyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date))
            val yearlyReport =
                requireNotNull(yearlyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date))

            dailyUserReportStorage.createOrUpdate(dailyReport.remove(moneyAction, { date }, ::DailyUserReport))
            monthlyUserReportStorage.createOrUpdate(monthlyReport.remove(moneyAction, { month }, ::MonthlyUserReport))
            yearlyUserReportStorage.createOrUpdate(yearlyReport.remove(moneyAction, { year }, ::YearlyUserReport))
        }
    }

    protected abstract fun <T, PeriodType> T.remove(
        moneyAction: MoneyAction,
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
    ): T where T : BaseUserReport, T : DomainModel
}
