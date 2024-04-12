package com.marsofandrew.bookkeeper.report

import com.marsofandrew.bookkeeper.properties.Money
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.user.User

interface BaseUserReport {
    val userId: NumericId<User>
    val expenses: Report<Category, PositiveMoney>
    val earnings: Report<Category, PositiveMoney>
    val transfers: Report<Category, Money>
    val total: List<Money>
}
