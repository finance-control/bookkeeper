package com.marsofandrew.bookkeeper.spending.controller.dto

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingUpdate
import java.time.LocalDate

internal data class UpdateSpendingDto(
    val money: AccountBoundedMoneyDto?,
    val date: LocalDate?,
    val description: String?,
    val categoryId: Long?,
    val version: Int
) {
    fun toSpendingUpdate(id: NumericId<Spending>) = SpendingUpdate(
        id = id,
        money = money?.money?.toPositiveMoney(),
        date = date,
        description = description,
        categoryId = categoryId?.asId(),
        sourceAccountId = money?.accountId?.asId(),
        version = Version(version)
    )
}
