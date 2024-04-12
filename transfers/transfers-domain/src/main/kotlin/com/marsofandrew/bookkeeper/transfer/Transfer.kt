package com.marsofandrew.bookkeeper.transfer

import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.transfer.category.Category
import com.marsofandrew.bookkeeper.transfer.user.User
import java.time.LocalDate

data class Transfer(
    override val id: NumericId<Transfer>,
    val userId: NumericId<User>,
    val date: LocalDate,
    val send: AccountMoney?,
    val received: AccountMoney,
    val description: String,
    val categoryId: NumericId<Category>,
    val createdAt: LocalDate,
    override val version: Version,
): DomainModel {
    init {
        validateFiled(createdAt >= LocalDate.of(2024, 1, 1)) { "Created at date is too early" }
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
    }
}
