package com.marsofandrew.bookkeeper.report.impl.util

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.BaseUserReport
import com.marsofandrew.bookkeeper.report.user.User

internal fun <ReportType : BaseUserReport, PeriodType> aggregate(
    userId: NumericId<User>,
    reports: Collection<ReportType>,
    periodProvider: ReportType.() -> PeriodType
): AggregatedUserReport<PeriodType> {
    if(reports.isEmpty()) return AggregatedUserReport.empty(userId)
    return reports.map {
        AggregatedUserReport.of(it) { listOf(it.periodProvider()) }
    }.reduce { acc, aggregatedUserReport -> acc + aggregatedUserReport }
}
