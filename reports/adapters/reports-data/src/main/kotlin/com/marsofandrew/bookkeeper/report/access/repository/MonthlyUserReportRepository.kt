package com.marsofandrew.bookkeeper.report.access.repository

import com.marsofandrew.bookkeeper.report.access.entity.DailyUserReportEntity
import com.marsofandrew.bookkeeper.report.access.entity.MonthlyUserReportEntity
import java.time.LocalDate
import java.time.YearMonth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface MonthlyUserReportRepository : JpaRepository<MonthlyUserReportEntity, MonthlyUserReportEntity.ReportId> {

    @Query(
        """
            SELECT * FROM bookkeeper.monthly_report
            WHERE user_id = :userId AND month BETWEEN :startMonth AND :endMonth
        """,
        nativeQuery = true
    )
    fun findAllByUserIdAndDateBetween(
        userId: Long,
        startMonth: YearMonth,
        endMonth: YearMonth
    ): List<MonthlyUserReportEntity>
}
