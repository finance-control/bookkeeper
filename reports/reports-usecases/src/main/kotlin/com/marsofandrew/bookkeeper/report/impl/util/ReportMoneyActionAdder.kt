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
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.common.WithDate
import com.marsofandrew.bookkeeper.report.common.WithUserId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year
import java.time.YearMonth

internal abstract class ReportMoneyActionAdder<MoneyAction>(
    private val dailyUserReportStorage: DailyUserReportStorage,
    private val monthlyUserReportStorage: MonthlyUserReportStorage,
    private val yearlyUserReportStorage: YearlyUserReportStorage,
    private val transactionExecutor: TransactionExecutor,
) where MoneyAction : WithUserId, MoneyAction : WithDate {

    fun add(moneyAction: MoneyAction) {
        transactionExecutor.execute {
            val dailyReport = dailyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date)
                ?.add(moneyAction, { date }, ::DailyUserReport)
                ?: createReport(moneyAction, { date }, ::DailyUserReport)
            val monthlyReport = monthlyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date)
                ?.add(moneyAction, { month }, ::MonthlyUserReport)
                ?: createReport(moneyAction, { YearMonth.from(date) }, ::MonthlyUserReport)
            val yearlyReport = yearlyUserReportStorage.findByUserIdAndDate(moneyAction.userId, moneyAction.date)
                ?.add(moneyAction, { year }, ::YearlyUserReport)
                ?: createReport(moneyAction, { Year.from(date) }, ::YearlyUserReport)


            dailyUserReportStorage.createOrUpdate(dailyReport)
            monthlyUserReportStorage.createOrUpdate(monthlyReport)
            yearlyUserReportStorage.createOrUpdate(yearlyReport)
        }
    }

    protected abstract fun <T, PeriodType> createReport(
        moneyAction: MoneyAction,
        period: MoneyAction.() -> PeriodType,
        creator: (
            userId: NumericId<User>,
            period: PeriodType,
            expenses: Report<SpendingCategory, PositiveMoney>,
            earnings: Report<TransferCategory, PositiveMoney>,
            transfers: Report<TransferCategory, Money>,
            total: List<Money>,
            version: Version
        ) -> T
    ): T where T : BaseUserReport, T : DomainModel

    protected abstract fun <T, PeriodType> T.add(
        moneyAction: MoneyAction,
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
    ): T where T : BaseUserReport, T : DomainModel
}
