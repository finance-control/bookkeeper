package com.marsofandrew.bookkeeper.report.access.repository

import com.marsofandrew.bookkeeper.report.access.entity.MonthlyUserReportEntity
import com.marsofandrew.bookkeeper.report.access.entity.YearlyUserReportEntity
import java.time.YearMonth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface YearlyUserReportRepository : JpaRepository<YearlyUserReportEntity, YearlyUserReportEntity.ReportId>{

    @Query(
        """
            SELECT * FROM bookkeeper.yearly_report
            WHERE user_id = :userId
        """,
        nativeQuery = true
    )
    fun findAllByUserId(
        userId: Long,
    ): List<YearlyUserReportEntity>
}
