package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.YearMonth

interface MonthlyUserReportStorage {

    fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): MonthlyUserReport?
    fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): List<MonthlyUserReport>

    fun createOrUpdate(report: MonthlyUserReport)
}