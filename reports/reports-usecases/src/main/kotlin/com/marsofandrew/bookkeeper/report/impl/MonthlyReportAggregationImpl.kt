package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.MonthlyReportAggregation
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

class MonthlyReportAggregationImpl(
    private val monthlyReportStorage: MonthlyReportStorage
) : MonthlyReportAggregation {

    override fun makeReport(
        userId: NumericId<User>,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): AggregatedUserReport<YearMonth> {
        return aggregate(monthlyReportStorage.findAllByUserIdBetween(userId, startMonth, endMonth)) { month }
    }
}