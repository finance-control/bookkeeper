package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReportAggregation
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

class MonthlyUserReportAggregationImpl(
    private val monthlyUserReportStorage: MonthlyUserReportStorage
) : MonthlyUserReportAggregation {

    override fun makeReport(
        userId: NumericId<User>,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): AggregatedUserReport<YearMonth> {
        return aggregate(userId, monthlyUserReportStorage.findAllByUserIdBetween(userId, startMonth, endMonth)) { month }
    }
}
