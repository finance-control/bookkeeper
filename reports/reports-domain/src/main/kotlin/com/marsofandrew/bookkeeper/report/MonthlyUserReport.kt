package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.YearMonth

data class MonthlyUserReport(
    override val userId: NumericId<User>,
    val month: YearMonth,
    override val expenses: Report<SpendingCategory, PositiveMoney>,
    override val earnings: Report<TransferCategory, PositiveMoney>,
    override val transfers: Report<TransferCategory, Money>,
    override val total: List<Money>
) : BaseUserReport
