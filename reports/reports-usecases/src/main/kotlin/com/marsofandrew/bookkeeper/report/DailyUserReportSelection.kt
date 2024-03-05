package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

interface DailyUserReportSelection {

    fun select(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<DailyUserReport>
}