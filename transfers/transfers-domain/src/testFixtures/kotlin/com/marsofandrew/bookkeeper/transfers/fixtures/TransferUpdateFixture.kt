package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.*
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.math.BigDecimal
import java.time.LocalDate

class TransferUpdateFixture(
    val id: NumericId<CommonTransfer>,
) {
    var date: LocalDate? = null
    var send: AccountMoney? = null
    var received: AccountMoney? = null
    var description: String? = null
    var categoryId: NumericId<Category>? = null
    var version = Version(0)

    fun build() = TransferUpdate(
        id,
        date,
        received,
        description,
        categoryId,
        send,
        version
    )
}
