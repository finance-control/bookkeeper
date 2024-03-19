package com.marsofandrew.bookkeeper.report.access.repository

import com.marsofandrew.bookkeeper.report.access.entity.DailyUserReportEntity
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface DailyUserReportRepository : JpaRepository<DailyUserReportEntity, DailyUserReportEntity.ReportId> {

    @Query(
        """
            SELECT * FROM bookkeeper.daily_report
            WHERE user_id = :userId AND date BETWEEN :startDate AND :endDate
        """,
        nativeQuery = true
    )
    fun findAllByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyUserReportEntity>
}
