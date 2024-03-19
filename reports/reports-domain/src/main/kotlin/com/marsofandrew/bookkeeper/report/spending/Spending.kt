package com.marsofandrew.bookkeeper.report.spending

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.SpendingCategory
import com.marsofandrew.bookkeeper.report.common.WithDate
import com.marsofandrew.bookkeeper.report.common.WithUserId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Spending(
    override val userId: NumericId<User>,
    val money: PositiveMoney,
    override val date: LocalDate,
    val categoryId: NumericId<SpendingCategory>,
): WithUserId, WithDate {

    init {
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
