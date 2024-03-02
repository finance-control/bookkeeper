package com.marsofandrew.bookkeeper.transfers

import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.transfers.category.TransferCategory
import com.marsofandrew.bookkeeper.transfers.user.User
import java.time.LocalDate

data class Transfer(
    val id: NumericId<Transfer>,
    val userId: NumericId<User>,
    val date: LocalDate,
    val send: AccountMoney?,
    val received: AccountMoney,
    val comment: String,
    val transferCategoryId: NumericId<TransferCategory>,
    val createdAt: LocalDate,
) {
    init {
        check(createdAt >= LocalDate.of(2024, 1, 1)) { "Created at date is too early" }
        check(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}

