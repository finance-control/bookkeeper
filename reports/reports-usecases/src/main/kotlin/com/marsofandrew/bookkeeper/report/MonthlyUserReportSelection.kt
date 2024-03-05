package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

interface MonthlyUserReportSelection {

    fun select(userId: NumericId<User>, startMonth: YearMonth, endMonth: YearMonth): List<MonthlyUserReport>
}