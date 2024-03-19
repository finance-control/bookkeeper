package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.DailyUserReportSelection
import com.marsofandrew.bookkeeper.report.access.DailyUserReportStorage
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

class DailyUserReportSelectionImpl(
    private val dailyUserReportStorage: DailyUserReportStorage
) : DailyUserReportSelection {

    override fun select(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<DailyUserReport> {
        require(startDate <= endDate) { "endDate less than start date" }

        return dailyUserReportStorage.findAllByUserIdBetween(userId, startDate, endDate)
    }
}
