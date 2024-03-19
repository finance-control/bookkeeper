package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.ObjectId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.Year

data class YearlyUserReport(
    override val userId: NumericId<User>,
    val year: Year,
    override val expenses: Report<SpendingCategory, PositiveMoney>,
    override val earnings: Report<TransferCategory, PositiveMoney>,
    override val transfers: Report<TransferCategory, Money>,
    override val total: List<Money>,
    override val version: Version
) : BaseUserReport, DomainModel {

    override val id: ObjectId<CombinedId> = ObjectId(CombinedId(userId, year))

    data class CombinedId(
        val userId: NumericId<User>,
        val year: Year
    )
}
