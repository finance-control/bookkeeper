package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

interface YearlyUserReportStorage {

    fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): YearlyUserReport?
    fun findAllByUserId(
        userId: NumericId<User>,
    ): List<YearlyUserReport>

    fun createOrUpdate(report: YearlyUserReport)
}