package com.marsofandrew.bookkeeper.report.access

import com.marsofandrew.bookkeeper.data.toModels
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.entity.YearlyUserReportEntity
import com.marsofandrew.bookkeeper.report.access.entity.toYearlyReportEntity
import com.marsofandrew.bookkeeper.report.access.repository.YearlyUserReportRepository
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate
import java.time.Year
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
internal class YearlyUserReportStorageImpl(
    private val yearlyUserReportRepository: YearlyUserReportRepository
) : YearlyUserReportStorage {
    override fun findByUserIdAndDate(userId: NumericId<User>, date: LocalDate): YearlyUserReport? {
        return yearlyUserReportRepository.findById(YearlyUserReportEntity.ReportId(userId.value, Year.from(date)))
            .getOrNull()
            ?.toModel()
    }

    override fun findAllByUserId(userId: NumericId<User>): List<YearlyUserReport> {
        return yearlyUserReportRepository.findAllByUserId(userId.value).toModels()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun createOrUpdate(report: YearlyUserReport) {
        yearlyUserReportRepository.saveAndFlush(report.toYearlyReportEntity())
    }
}
