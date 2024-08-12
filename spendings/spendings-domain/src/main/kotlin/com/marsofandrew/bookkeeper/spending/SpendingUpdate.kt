package com.marsofandrew.bookkeeper.spending

import com.marsofandrew.bookkeeper.base.model.DomainModel
import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.exception.validateFiled
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.Category
import java.time.LocalDate

data class SpendingUpdate(
    override val id: NumericId<Spending>,
    val money: PositiveMoney?,
    val date: LocalDate?,
    val description: String?,
    val categoryId: NumericId<Category>?,
    val sourceAccountId: StringId<Account>?,
    override val version: Version
) : DomainModel {
    init {
        validateFiled(date == null || date >= LocalDate.of(2022, 1, 1)) { "Dates before 2022-01-01 are not supported" }
        validateFiled(categoryId?.initialized ?: true) { "categoryId is not initialized" }
    }
}

fun SpendingUpdate.toSpending(original: Spending): Spending = Spending(
    id = id,
    userId = original.userId,
    money = money ?: original.money,
    date = date ?: original.date,
    description = description ?: original.description,
    categoryId = categoryId ?: original.categoryId,
    createdAt = original.createdAt,
    sourceAccountId = sourceAccountId ?: original.sourceAccountId,
    version = version
)
