package com.marsofandrew.bookkeeper.spending.fixtures

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.SpendingCategory
import com.marsofandrew.bookkeeper.spending.user.User
import java.math.BigDecimal
import java.time.LocalDate

class SpendingFixture(
    val id: NumericId<Spending>,
    val userId: NumericId<User>,
) {
    var money: PositiveMoney = PositiveMoney(Currency.RUB, BigDecimal.valueOf(10))
    var date: LocalDate = LocalDate.now()
    var description: String = "test"
    var spendingCategoryId: NumericId<SpendingCategory> = 1.asId()
    var createdAt: LocalDate = LocalDate.now()
    var fromAccount: StringId<Account>? = null

    fun build() = Spending(
        id,
        userId,
        money,
        date,
        description,
        spendingCategoryId,
        createdAt,
        fromAccount
    )
}
