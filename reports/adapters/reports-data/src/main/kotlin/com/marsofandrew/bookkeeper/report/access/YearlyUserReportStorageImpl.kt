package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.Year
import org.springframework.stereotype.Service

@Service
internal class YearlyUserReportStorageImpl(
    private val reportsByUserId: MutableMap<NumericId<User>, List<YearlyUserReport>> = mutableMapOf()
) : YearlyUserReportStorage {
    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): YearlyUserReport? {
        return reportsByUserId[userId]?.find { it.year == Year.from(date) }
    }

    override fun findAllByUserId(userId: NumericId<User>): List<YearlyUserReport> {
        return reportsByUserId.getOrDefault(userId, emptyList())
    }

    override fun createOrUpdate(report: YearlyUserReport) {
        reportsByUserId[report.userId] = reportsByUserId.getOrDefault(report.userId, mutableListOf()) + report
    }
}