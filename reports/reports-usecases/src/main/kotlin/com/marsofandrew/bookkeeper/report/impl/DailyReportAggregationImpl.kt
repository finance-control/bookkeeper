package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.DailyReportAggregation
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

class DailyReportAggregationImpl(
    private val dailyReportStorage: DailyReportStorage
) : DailyReportAggregation {

    override fun makeReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): AggregatedUserReport<LocalDate> {
        return aggregate(dailyReportStorage.findAllByUserIdBetween(userId, startDate, endDate)) { date }
    }
}