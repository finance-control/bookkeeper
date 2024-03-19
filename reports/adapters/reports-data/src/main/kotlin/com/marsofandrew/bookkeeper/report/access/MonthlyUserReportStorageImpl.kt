package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.MonthlyUserReport
import com.marsofandrew.bookkeeper.report.access.entity.MonthlyUserReportEntity
import com.marsofandrew.bookkeeper.report.access.entity.toMonthlyReportEntity
import com.marsofandrew.bookkeeper.report.access.repository.MonthlyUserReportRepository
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.YearMonth
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
internal class MonthlyUserReportStorageImpl(
    private val monthlyUserReportRepository: MonthlyUserReportRepository
) : MonthlyUserReportStorage {
    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): MonthlyUserReport? {
        return monthlyUserReportRepository.findById(
            MonthlyUserReportEntity.ReportId(
                userId = userId.value,
                month = YearMonth.from(date)
            )
        ).getOrNull()?.toModel()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): List<MonthlyUserReport> {
        return monthlyUserReportRepository.findAllByUserIdAndDateBetween(userId.value, startMonth, endMonth).toModels()
    }

    @Transactional
    override fun createOrUpdate(report: MonthlyUserReport) {
        monthlyUserReportRepository.saveAndFlush(report.toMonthlyReportEntity())
    }
}