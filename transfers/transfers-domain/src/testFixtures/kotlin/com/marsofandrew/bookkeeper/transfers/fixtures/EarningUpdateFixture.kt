package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.Earning
import com.marsofandrew.bookkeeper.transfers.EarningUpdate
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.math.BigDecimal
import java.time.LocalDate

class EarningUpdateFixture(
    val id: NumericId<CommonTransfer>,
) {
    var date: LocalDate? = null
    var received: AccountMoney? = null
    var description: String? = null
    var categoryId: NumericId<Category>? = null
    var version = Version(0)

    fun build() = EarningUpdate(
        id,
        date,
        received,
        description,
        categoryId,
        version
    )
}
