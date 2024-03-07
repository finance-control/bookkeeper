package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import org.springframework.stereotype.Service

@Service
internal class DailyUserReportStorageImpl(
    private val reportsByUserId: MutableMap<NumericId<User>, List<DailyUserReport>> = mutableMapOf()
) : DailyUserReportStorage {

    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): DailyUserReport? {
        return reportsByUserId[userId]?.find { it.date == date }
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyUserReport> {
        return reportsByUserId[userId]?.filter { it.date in startDate..endDate } ?: emptyList()
    }

    override fun createOrUpdate(report: DailyUserReport) {
        reportsByUserId[report.userId] = reportsByUserId.getOrDefault(report.userId, mutableListOf()) + report
    }
}