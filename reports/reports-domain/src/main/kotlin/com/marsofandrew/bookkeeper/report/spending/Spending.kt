package com.marsofandrew.bookkeeper.report.spending

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Spending(
    val userId: NumericId<User>,
    val money: PositiveMoney,
    val date: LocalDate,
    val spendingCategoryId: NumericId<SpendingCategory>,
) {

    init {
        check(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
