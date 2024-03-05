package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReportSelection
import com.marsofandrew.bookkeeper.report.access.DailyReportStorage
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

class DailyUserReportSelectionImpl(
    private val dailyReportStorage: DailyReportStorage
) : DailyUserReportSelection {

    override fun select(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<DailyUserReport> {
        return dailyReportStorage.findAllByUserIdBetween(userId, startDate, endDate)
    }
}