package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.exception.validateFiled
import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory
import com.marsofandrew.bookkeeper.spending.user.User
import java.time.LocalDate

data class Spending(
    override val id: NumericId<Spending>,
    val userId: NumericId<User>,
    val money: PositiveMoney,
    val date: LocalDate,
    val description: String,
    val spendingCategoryId: NumericId<SpendingCategory>,
    val createdAt: LocalDate,
    val fromAccount: StringId<Account>?,
    override val version: Version
) : DomainModel {
    init {
        validateFiled(createdAt >= LocalDate.of(2024, 1, 1)) { "Created at date is too early" }
        validateFiled(date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
        validateFiled(spendingCategoryId.initialized) { "spendingCategoryId is not initialized" }
    }
}
