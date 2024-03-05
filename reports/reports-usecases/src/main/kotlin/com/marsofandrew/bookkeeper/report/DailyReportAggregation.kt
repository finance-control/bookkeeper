package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

interface DailyReportAggregation {

    fun makeReport(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): AggregatedUserReport<LocalDate>
}