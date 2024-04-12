package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.ObjectId
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

data class MonthlyUserReport(
    override val userId: NumericId<User>,
    val month: YearMonth,
    override val expenses: Report<Category, PositiveMoney>,
    override val earnings: Report<Category, PositiveMoney>,
    override val transfers: Report<Category, Money>,
    override val total: List<Money>,
    override val version: Version
) : BaseUserReport, DomainModel {

    override val id: ObjectId<CombinedId> = ObjectId(CombinedId(userId, month))

    data class CombinedId(
        val userId: NumericId<User>,
        val month: YearMonth
    )
}
