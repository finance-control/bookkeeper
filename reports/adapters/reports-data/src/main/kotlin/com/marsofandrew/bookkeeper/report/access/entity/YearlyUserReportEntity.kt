package com.marsofandrew.bookkeeper.report.access.entity

import com.marsofandrew.bookkeeper.data.BaseEntity
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.properties.util.toPositiveMoney
import com.marsofandrew.bookkeeper.report.YearlyUserReport
import com.marsofandrew.bookkeeper.report.access.entity.dto.MoneyDto
import com.marsofandrew.bookkeeper.report.access.entity.dto.ReportDto
import com.marsofandrew.bookkeeper.report.access.entity.dto.toMoney
import com.marsofandrew.bookkeeper.report.access.entity.dto.toMoneyDto
import com.marsofandrew.bookkeeper.report.access.entity.dto.toReport
import com.marsofandrew.bookkeeper.report.access.entity.dto.toReportDto
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.io.Serializable
import java.time.Year
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "yearly_report")
internal data class YearlyUserReportEntity(
    @EmbeddedId
    var reportId: ReportId,
    @JdbcTypeCode(SqlTypes.JSON)
    var expenses: ReportDto,
    @JdbcTypeCode(SqlTypes.JSON)
    var earnings: ReportDto,
    @JdbcTypeCode(SqlTypes.JSON)
    var transfers: ReportDto,
    @JdbcTypeCode(SqlTypes.JSON)
    var total: List<MoneyDto>,
    @Version
    var version: Int
) : BaseEntity<YearlyUserReport> {

    @Embeddable
    data class ReportId(
        val userId: Long,
        val year: Year
    ) : Serializable

    override fun toModel(): YearlyUserReport = YearlyUserReport(
        userId = reportId.userId.asId(),
        year = reportId.year,
        expenses = expenses.toReport { toPositiveMoney() },
        earnings = earnings.toReport { toPositiveMoney() },
        transfers = earnings.toReport { this },
        total = total.map { it.toMoney() },
        version = com.marsofandrew.bookkeeper.base.model.Version(version)
    )
}

internal fun YearlyUserReport.toYearlyReportEntity() = YearlyUserReportEntity(
    reportId = YearlyUserReportEntity.ReportId(userId.value, year),
    expenses = expenses.toReportDto(),
    earnings = earnings.toReportDto(),
    transfers = transfers.toReportDto(),
    total = total.map { it.toMoneyDto() },
    version = version.value
)
