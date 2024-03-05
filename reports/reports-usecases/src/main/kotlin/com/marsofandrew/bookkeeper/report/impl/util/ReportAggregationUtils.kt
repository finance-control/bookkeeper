package com.marsofandrew.bookkeeper.report.impl.util

import com.marsofandrew.bookkeeper.report.AggregatedUserReport
import com.marsofandrew.bookkeeper.report.BaseUserReport

internal fun <ReportType : BaseUserReport, PeriodType> aggregate(
    reports: Collection<ReportType>,
    periodProvider: ReportType.() -> PeriodType
): AggregatedUserReport<PeriodType> {
    return reports.map {
        AggregatedUserReport.of(it) { listOf(it.periodProvider()) }
    }.reduce { acc, aggregatedUserReport -> acc + aggregatedUserReport }
}
