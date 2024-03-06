package com.marsofandrew.bookkeeper.report.earning

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Earning(
    val userId: NumericId<User>,
    val date: LocalDate,
    val money: PositiveMoney,
    val transferCategoryId: NumericId<TransferCategory>,
) {
    init {
        check(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
