package com.marsofandrew.bookkeeper.report.transfer

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.report.category.TransferCategory
import com.marsofandrew.bookkeeper.report.common.WithDate
import com.marsofandrew.bookkeeper.report.common.WithUserId
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class Transfer(
    override val userId: NumericId<User>,
    override val date: LocalDate,
    val send: PositiveMoney,
    val received: PositiveMoney,
    val categoryId: NumericId<TransferCategory>,
) : WithUserId, WithDate {

    init {
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
