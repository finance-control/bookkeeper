package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

interface DailyReportStorage {

    fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): DailyUserReport?
    fun findAllByUserIdBetween(userId: NumericId<User>, startDate: LocalDate, endDate: LocalDate): List<DailyUserReport>
    fun createOrUpdate(report: DailyUserReport): DailyUserReport
}