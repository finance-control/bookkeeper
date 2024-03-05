package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.utils.sumOfNullable
import com.marsofandrew.bookkeeper.base.utils.summarize
import com.marsofandrew.bookkeeper.properties.BaseMoney
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User

data class AggregatedUserReport<PeriodType>(
    override val userId: NumericId<User>,
    val periods: List<PeriodType>,
    override val expenses: Report<SpendingCategory, PositiveMoney>,
    override val earnings: Report<TransferCategory, PositiveMoney>,
    override val transfers: Report<TransferCategory, Money>,
    override val total: List<Money>
) : BaseUserReport {

    operator fun plus(other: AggregatedUserReport<PeriodType>): AggregatedUserReport<PeriodType> {
        require(other.userId == userId) { "userIds are different" }

        return AggregatedUserReport(
            userId = userId,
            periods = (periods + other.periods),
            expenses = expenses + other.expenses,
            earnings = earnings + other.earnings,
            transfers = transfers + other.transfers,
            total = summarize(total, other.total).toList(),
        )
    }

    companion object {

        fun <PeriodType> of(
            report: BaseUserReport,
            periodProvider: BaseUserReport.() -> List<PeriodType>
        ): AggregatedUserReport<PeriodType> = AggregatedUserReport(
            userId = report.userId,
            periods = report.periodProvider(),
            expenses = report.expenses,
            earnings = report.earnings,
            transfers = report.transfers,
            total = report.total,
        )
    }
}
