package com.marsofandrew.bookkeeper.report.fixture

import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.report.category.Category
import com.marsofandrew.bookkeeper.report.earning.Earning
import com.marsofandrew.bookkeeper.report.user.User
import java.time.LocalDate

data class EarningFixture(
    val userId: NumericId<User>,
) {
    var money: PositiveMoney = PositiveMoney(Currency.EUR, 100)
    var date: LocalDate = LocalDate.now()
    var categoryId: NumericId<Category> = 5.asId()

    fun build() = Earning(
        userId = userId,
        money = money,
        date = date,
        categoryId = categoryId
    )
}
