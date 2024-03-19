package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

fun dailyUserReport(userId: NumericId<User>, date: LocalDate, init: DailyUserReportFixture.() -> Unit = {}) =
    DailyUserReportFixture(userId, date).apply(init).build()

fun monthlyUserReport(userId: NumericId<User>, month: YearMonth, init: MonthlyUserReportFixture.() -> Unit = {}) =
    MonthlyUserReportFixture(userId, month).apply(init).build()

fun yearlyUserReport(userId: NumericId<User>, year: Year, init: YearlyUserReportFixture.() -> Unit = {}) =
    YearlyUserReportFixture(userId, year).apply(init).build()

fun spending(userId: NumericId<User>, init: SpendingFixture.() -> Unit = {}) =
    SpendingFixture(userId).apply(init).build()

fun transfer(userId: NumericId<User>, init: TransferFixture.() -> Unit = {}) =
    TransferFixture(userId).apply(init).build()

fun earning(userId: NumericId<User>, init: EarningFixture.() -> Unit = {}) =
    EarningFixture(userId).apply(init).build()

fun <PeriodType> aggregatedReport(
    userId: NumericId<User>,
    periods: List<PeriodType>,
    init: AggregatedUserReportFixture<PeriodType>.() -> Unit = {}
): AggregatedUserReport<PeriodType> =
    AggregatedUserReportFixture(userId, periods).apply(init).build()
