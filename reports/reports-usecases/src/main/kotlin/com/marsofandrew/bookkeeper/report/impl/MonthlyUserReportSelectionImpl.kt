package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReportSelection
import com.marsofandrew.bookkeeper.report.access.MonthlyReportStorage
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

class MonthlyUserReportSelectionImpl(
    private val monthlyReportStorage: MonthlyReportStorage
) : MonthlyUserReportSelection {

    override fun select(userId: NumericId<User>, startMonth: YearMonth, endMonth: YearMonth): List<MonthlyUserReport> {
        return monthlyReportStorage.findAllByUserIdBetween(userId, startMonth, endMonth)
    }
}