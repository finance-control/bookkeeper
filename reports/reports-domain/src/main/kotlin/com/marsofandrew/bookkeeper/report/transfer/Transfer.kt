package com.marsofandrew.bookkeeper.report.transfer

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Transfer(
    val userId: NumericId<User>,
    val date: LocalDate,
    val send: PositiveMoney?,
    val received: PositiveMoney,
    val transferCategoryId: NumericId<TransferCategory>,
) {
    init {
        check(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
