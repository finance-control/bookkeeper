package com.marsofandrew.bookkeeper.report.impl

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.MonthlyUserReportSelection
import com.marsofandrew.bookkeeper.report.access.MonthlyUserReportStorage
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

class MonthlyUserReportSelectionImpl(
    private val monthlyUserReportStorage: MonthlyUserReportStorage
) : MonthlyUserReportSelection {

    override fun select(userId: NumericId<User>, startMonth: YearMonth, endMonth: YearMonth): List<MonthlyUserReport> {
        require(startMonth <= endMonth) { "endMonth less than startMonth" }
        return monthlyUserReportStorage.findAllByUserIdBetween(userId, startMonth, endMonth)
    }
}