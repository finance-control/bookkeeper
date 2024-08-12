package com.marsofandrew.bookkeeper.spending.fixture

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.StringId
import com.marsofandrew.bookkeeper.spending.Spending
import com.marsofandrew.bookkeeper.spending.SpendingUpdate
import com.marsofandrew.bookkeeper.spending.account.Account
import com.marsofandrew.bookkeeper.spending.category.Category
import java.time.LocalDate

data class SpendingUpdateFixture(
    val id: NumericId<Spending>,
) {
    var money: PositiveMoney? = null
    var date: LocalDate? = null
    var description: String? = null
    var categoryId: NumericId<Category>? = null
    var sourceAccountId: StringId<Account>? = null
    var version: Version = Version(0)

    fun build() = SpendingUpdate(
        id,
        money,
        date,
        description,
        categoryId,
        sourceAccountId,
        version
    )
}
