package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class DailyUserReport(
    override val userId: NumericId<User>,
    val date: LocalDate,
    override val expenses: Report<SpendingCategory, PositiveMoney>,
    override val earnings: Report<TransferCategory, PositiveMoney>,
    override val transfers: Report<TransferCategory, Money>,
    override val total: List<Money>
) : BaseUserReport
