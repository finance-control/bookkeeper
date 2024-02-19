package com.marsofandrew.bookkeeper.spendings.fixtures

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spendings.Spending
import com.marsofandrew.bookkeeper.spendings.category.SpendingCategory
import com.marsofandrew.bookkeeper.spendings.user.User
import java.math.BigDecimal
import java.time.LocalDate

class SpendingFixture(
    val id: StringId<Spending>,
    val userId: NumericId<User>,
) {
    var money: PositiveMoney = PositiveMoney(Currency.RUB, BigDecimal.valueOf(10))
    var date: LocalDate = LocalDate.now()
    var comment: String = "test"
    var spendingCategoryId: StringId<SpendingCategory> = "test".asId()

    fun build() = Spending(
        id,
        userId,
        money,
        date,
        comment,
        spendingCategoryId
    )
}
