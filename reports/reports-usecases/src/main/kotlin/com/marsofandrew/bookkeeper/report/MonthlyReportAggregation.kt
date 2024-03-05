package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

interface MonthlyReportAggregation {

    fun makeReport(userId: NumericId<User>, startMonth: YearMonth, endMonth: YearMonth): AggregatedUserReport<YearMonth>
}