package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.YearMonth
import org.springframework.stereotype.Service

@Service
internal class MonthlyUserReportStorageImpl(
    private val reportsByUserId: MutableMap<NumericId<User>, List<MonthlyUserReport>> = mutableMapOf()
) : MonthlyUserReportStorage {
    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): MonthlyUserReport? {
        return reportsByUserId[userId]?.find { it.month == YearMonth.from(date) }
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): List<MonthlyUserReport> {
        return reportsByUserId[userId]?.filter { it.month in startMonth..endMonth } ?: emptyList()
    }

    override fun createOrUpdate(report: MonthlyUserReport) {
        reportsByUserId[report.userId] = reportsByUserId.getOrDefault(report.userId, mutableListOf()) + report
    }
}