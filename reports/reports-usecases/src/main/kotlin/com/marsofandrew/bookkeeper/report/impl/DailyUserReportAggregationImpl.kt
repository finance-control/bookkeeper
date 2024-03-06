package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReportAggregation
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.impl.util.aggregate
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

class DailyUserReportAggregationImpl(
    private val dailyUserReportStorage: DailyUserReportStorage
) : DailyUserReportAggregation {

    override fun makeReport(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): AggregatedUserReport<LocalDate> {
        return aggregate(userId, dailyUserReportStorage.findAllByUserIdBetween(userId, startDate, endDate)) { date }
    }
}