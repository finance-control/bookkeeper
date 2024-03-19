package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User

interface BaseUserReport {
    val userId: NumericId<User>
    val expenses: Report<SpendingCategory, PositiveMoney>
    val earnings: Report<TransferCategory, PositiveMoney>
    val transfers: Report<TransferCategory, Money>
    val total: List<Money>
}
