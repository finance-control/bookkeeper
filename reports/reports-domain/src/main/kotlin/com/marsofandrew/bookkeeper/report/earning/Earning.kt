package com.marsofandrew.bookkeeper.report.earning

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.common.WithDate
import com.marsofandrew.bookkeeper.report.common.WithUserId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Earning(
    override val userId: NumericId<User>,
    override val date: LocalDate,
    val money: PositiveMoney,
    val categoryId: NumericId<Category>,
): WithUserId, WithDate {
    init {
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
