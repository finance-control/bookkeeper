package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.DailyUserReport
import com.marsofandrew.bookkeeper.report.access.entity.DailyUserReportEntity
import com.marsofandrew.bookkeeper.report.access.entity.toDailyReportEntity
import com.marsofandrew.bookkeeper.report.access.repository.DailyUserReportRepository
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
internal class DailyUserReportStorageImpl(
    private val dailyUserReportRepository: DailyUserReportRepository,
) : DailyUserReportStorage {

    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): DailyUserReport? {
        return dailyUserReportRepository.findById(DailyUserReportEntity.ReportId(userId.value, date)).getOrNull()
            ?.toModel()
    }

    override fun findAllByUserIdBetween(
        userId: NumericId<User>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DailyUserReport> {
        return dailyUserReportRepository.findAllByUserIdAndDateBetween(userId.value, startDate, endDate).toModels()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun createOrUpdate(report: DailyUserReport) {
        dailyUserReportRepository.saveAndFlush(report.toDailyReportEntity())
    }
}
