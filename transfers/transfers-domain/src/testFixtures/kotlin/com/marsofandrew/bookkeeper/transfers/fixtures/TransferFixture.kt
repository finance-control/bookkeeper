package com.marsofandrew.bookkeeper.transfers.fixtures

import com.marsofandrew.bookkeeper.base.model.Version
import com.marsofandrew.bookkeeper.properties.Currency
import com.marsofandrew.bookkeeper.properties.PositiveMoney
import com.marsofandrew.bookkeeper.properties.id.NumericId
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.transfers.AccountMoney
import com.marsofandrew.bookkeeper.transfers.CommonTransfer
import com.marsofandrew.bookkeeper.transfers.Transfer
import com.marsofandrew.bookkeeper.transfers.category.Category
import com.marsofandrew.bookkeeper.transfers.user.User
import java.math.BigDecimal
import java.time.LocalDate

class TransferFixture(
    val id: NumericId<CommonTransfer>,
    val userId: NumericId<User>,
) {
    var date: LocalDate = LocalDate.now()
    var send: AccountMoney = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.ONE))
    var received: AccountMoney = AccountMoney.create(PositiveMoney(Currency.EUR, BigDecimal.ONE))
    var description: String = ""
    var categoryId: NumericId<Category> = 1.asId()
    var createdAt: LocalDate = LocalDate.now()
    var version = Version(0)

    fun build() = Transfer(
        id,
        userId,
        date,
        send,
        received,
        description,
        categoryId,
        createdAt,
        version
    )
}