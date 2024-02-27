package com.marsofandrew.bookkeeper.spendings

import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory
import com.marsofandrew.bookkeeper.spendings.user.User
import java.time.LocalDate

data class Spending(
    val id: StringId<Spending>,
    val userId: NumericId<User>,
    val money: PositiveMoney,
    val date: LocalDate,
    val comment: String,
    val spendingCategoryId: NumericId<SpendingCategory>
)
